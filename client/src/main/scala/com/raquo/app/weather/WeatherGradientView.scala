package com.raquo.app.weather

import com.raquo.airstream.core.Observable
import com.raquo.app.JsRouter.*
import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.app.jsonApiDecoder
import com.raquo.app.pages.WeatherGradientPage
import com.raquo.data.ApiResponse
import com.raquo.laminar.api.L.*
import com.raquo.utils.Url.*
import com.raquo.utils.Utils.useImport
import com.raquo.weather.{Gradient, GradientReport}
import vendor.chartjs.*

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// BEGIN[wind-gradient]
object WeatherGradientView {

  // Find and import the LESS (CSS) file for this component.
  // See https://github.com/raquo/vite-plugin-glob-resolver
  // See https://github.com/raquo/vite-plugin-import-side-effect
  @JSImport("@find/**/WeatherGradientView.less", JSImport.Namespace)
  @js.native private object Stylesheet extends js.Object

  useImport(Stylesheet)

  // Chart.js requires you to load the necessary components this way
  // to avoid loading the entire library. It will also tell you which
  // components are needed if you forget to include them.
  Chart.register(
    ChartDataLabelsPlugin,
    Legend,
    //BarController,
    //BarElement,
    LineController,
    LineElement,
    PointElement,
    CategoryScale,
    LinearScale,
  )

  def apply(pageS: Signal[WeatherGradientPage]): HtmlElement = {

    // Just a naming convention: if a variable ends in a capital S, it's a Signal or Stream.
    // Helps to differentiate Scala collections from observables, and their mixes:
    // e.g. users: List[user], userS: Signal[User], usersS: Signal[List[User]]
    val gradientS = pageS.flatMapSwitch { p =>
      Gradient.forId(p.gradientId) match {
        case Some(gradient) =>
          EventStream.fromValue(gradient)
        case None =>
          js.timers.setTimeout(0) {
            // #TODO the delay is needed in case when you're serving the production application,
            //  and the user navigates to a bad gradient wind URL via the address bar (as opposed
            //  to using waypoint / pushState). Not quire sure why, must be some race condition.
            //  Ideally Waypoint should have better built-in support for async route validation.
            //  Actually it would be good to show how to do both sync and async route validation.
            forcePage(NotFoundPage)
          }
          EventStream.empty
      }
    }

    val apiResponseS = gradientS.flatMapSwitch { gradient =>
      FetchStream
        .withDecoder(jsonApiDecoder[GradientReport])
        .get(absRoot / "api" / "weather" / "gradient" / gradient.id)
    }
    // collect-ing the stream simply skips all events that don't match
    // (errors, in this case). Note: you can't `filter` or `collect`
    // SIGNALS because they must always have a current value.
    // Read Airstream docs to understand why, and how to do what you need.
    val gradientReportStream = apiResponseS.collect {
      case ApiResponse.Result(report) => report
    }

    div(
      // attr(value) is alias for attr := value
      // `cls` is alias for `className` – this HTML DOM property sets CSS class name on this element
      cls("WeatherGradientView"),
      h1(
        child.text <-- gradientS.map(_.name + " gradient")
      ),
      renderOtherGradients(gradientS),
      renderError(apiResponseS),
      child <-- gradientReportStream.toWeakSignal.splitOption(
        (initialReport, reportSignal) => renderChart(initialReport, reportSignal),
        ifEmpty = div(cls("-loading"), "Fetching data...")
      ),
      CodeSnippets(
        _.`wind-gradient`.sortBy(_.fileName != "WeatherGradientView.scala"),
        startExpanded = _.fileName == "WeatherGradientView.scala"
      )
    )
  }

  private def renderOtherGradients(
    gradientS: EventStream[Gradient]
  ): Modifier.Base = {
    // We use implicit conversion here to convert a list of modifiers into one modifier
    List[Modifier.Base](
      "See also: ",
      children <-- gradientS.map { gradient =>
        val otherGradients = Gradient.values.filterNot(_ == gradient)
        otherGradients.flatMap { g =>
          List[Node]( // Node is the common type of elements and text nodes
            a(g.name, navigateTo(WeatherGradientPage(g.id))),
            ", "
          )
        }.toList.init
      }
    )
  }

  // This function is only called once (when gradientReportStream emits
  // the network response), so all the elements that it creates are created
  // only once. Remember – no virtual DOM in Laminar.
  // Then, we use lifecycle hooks and --> methods to bring reactivity to
  // those elements that we created, so that, for example, when you click
  // on a tab for a different day, we don't re-render the whole chart from
  // scratch, but rather we update it with new data using Chart.js update API.
  private def renderChart(
    initialReport: GradientReport,
    gradientReportS: Signal[GradientReport]
  ): HtmlElement = {
    val selectedDayVar = Var(Option.empty[String]) // "None" means "Current observations"
    var maybeChart = Option.empty[Chart]
    div(
      Tabs(
        initialReport.forecastDays,
        maybeForecastDay => {
          List(
            // Setting class name dynamically
            cls("x-selected") <-- selectedDayVar.signal.map(_ == maybeForecastDay),
            // When this tab button is clicked, send the corresponding day into the var
            onClick.mapTo(maybeForecastDay) --> selectedDayVar
          )
        }
      ),
      div(
        // Don't put anything else in this div except for the canvas tag!
        // That's a Chart.js requirement for responsive sizing.
        cls("-graphbox"),
        canvasTag(
          // This lifecycle block instantiates the chart when this element is mounted,
          // and frees up its resources when it's unmounted. This way you can navigate
          // in and out of the graph page without using up more resources than needed.
          onMountUnmountCallback(
            mount = ctx => {
              maybeChart = Some(WeatherGradientChart.makeChart(ctx.thisNode.ref))
            },
            unmount = _ => maybeChart.foreach(_.destroy())
          ),
          // The callback below is invoked whenever you select a different date, or whenever
          // we get new data from the server (the latter only happens once in this application).
          // As you see we are not re-creating the chart here on every update, rather,
          // we reuse the existing chart, pushing updated config to it. This is much more
          // efficient and gives users a better experience.
          //
          // Note: mounting (see onMountUnmountCallback above) is synchronous, and the network
          // request to fetch graph data is asynchronous, so the latter is guaranteed to happen
          // AFTER this mounting callback. In this callback we implicitly rely on this sequence
          // of events, because if it was the other way round, maybeChart here would be empty,
          // and this callback wouldn't do anything. You always need to consider such things
          // when building UI applications, but thankfully JS world is single threaded, so it's
          // much easier to reason about.
          selectedDayVar.signal.combineWith(gradientReportS) --> { (selectedDay, report) =>
            maybeChart.foreach { chart =>
              WeatherGradientChart.updateChart(chart, selectedDay, report)
            }
          }
        ),
      )
    )
  }



  // Your render functions don't need to return just elements, for example
  // this one returns a Modifier that inserts a dynamic child node wherever
  // it's put.
  private def renderError(apiResponseS: EventStream[ApiResponse[_]]): Mod[Element] = {
    val maybeApiErrorS = apiResponseS.collect {
      case ApiResponse.Error(msg, _) => Some(msg)
      case _ => None
    }
    child.maybe <-- maybeApiErrorS.map(_.map { err =>
      div(textAlign.center, span(cls("u-error"), err))
    })
  }


}
// END[wind-gradient]
