package com.raquo.app.weather

import com.raquo.airstream.core.Observable
import com.raquo.app.JsRouter.*
import com.raquo.app.jsonApiDecoder
import com.raquo.app.pages.WeatherGradientPage
import com.raquo.data.ApiResponse
import com.raquo.laminar.api.L.*
import com.raquo.utils.{DynamicJsObject, JsImportSideEffect}
import com.raquo.utils.Url.*
import com.raquo.utils.Utils.*
import com.raquo.weather.{Gradient, GradientReport}
import org.scalajs.dom
import vendor.chartjs.*

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

object WeatherGradientView {

  // Find and import the LESS (CSS) file for this component. See globResolverPlugin and importSideEffectPlugin
  JsImportSideEffect("@find/**/WeatherGradientView.less")

  // Chart.js requires you to load the necessary components this way
  // to avoid loading the entire library. It will also tell you which
  // components are needed if you forget to include them.
  Chart.register(
    BarController,
    BarElement,
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
    // #Note: `def` is important here, but hopefully not for long, looking to fix it in 17.0.0
    def gradientS = pageS.flatMap { p =>
      Gradient.forId(p.gradientId) match {
        case Some(gradient) =>
          EventStream.fromValue(gradient)
        case None =>
          forcePage(NotFoundPage)
          EventStream.empty
      }
    }

    val apiResponseS = gradientS.flatMap { gradient =>
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
        ifEmpty = div("Loading data...")
      )
    )
  }

  private def renderOtherGradients(
    gradientS: EventStream[Gradient]
  ): Modifier.Base = {
    children <-- gradientS.map { gradient =>
      val otherGradients = Gradient.values.filterNot(_ == gradient)
      otherGradients.map { g =>
        a(g.name, navigateTo(WeatherGradientPage(g.id)))
      }.toList
    }
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
      cls("-graphbox"),
      Tabs(
        initialReport.forecastDays,
        maybeForecastDay => {
          List(
            // Setting class name dynamically
            cls.toggle("x-selected") <-- selectedDayVar.signal.map(_ == maybeForecastDay),
            // When this tab button is clicked, send the corresponding day into the var
            onClick.mapTo(maybeForecastDay) --> selectedDayVar
          )
        }
      ),
      canvasTag(
        // This lifecycle block instantiates the chart when this element is mounted,
        // and frees up its resources when it's unmounted. This way you can navigate
        // in and out of the graph page without using up more resources than needed.
        onMountUnmountCallback(
          mount = ctx => {
            maybeChart = Some(makeChart(ctx.thisNode.ref)) //, selectedDay.signal, gradientReportS))
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
            updateChart(chart, selectedDay, report)
          }
        }
      ),
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

  private def makeChart(
    canvasElement: dom.html.Canvas
  ): Chart = {
    // Note: most data and configuration is updated when the graph data is received
    new Chart(
      canvas = canvasElement,
      config = new ChartConfig(
        data = new ChartData(),
        options = new ChartConfigOptions(
          animation = false
        )
      )
    )
  }

  private def updateChart(chart: Chart, selectedDay: Option[String], report: GradientReport): Unit = {
    val cityNames = report.cities.toJSArray.map(_.name)

    val (temperatures, pressures, datasets) = selectedDay match {
      case None =>
        // Current weather
        val temperatures = report.selectCurrentConditions(_.temperatureC)
        val pressures = report.selectCurrentConditions(_.pressureKPa)
        (
          temperatures.flatten,
          pressures.flatten,
          js.Array(
            temperatureDatasetConfig(temperatures.map(_.orNull).toJSArray),
            pressureDatasetConfig(pressures.map(_.orNull).toJSArray)
          )
        )
      case Some(forecastDay) =>
        val temperatures = report.selectForecasts(forecastDay, _.temperatureC.some)
        val datasets = js.Array(
          temperatureDatasetConfig(temperatures.map(_.orNull).toJSArray)
        )
        (temperatures.flatten, Nil, datasets)
    }

    val axes = scalesConfig(temperatures, pressures)

    // To update chart.js graph, you need to mutate its config,
    // then call chart.update() for it to take effect.
    chart.config.data.labels = cityNames
    chart.config.data.datasets = datasets
    chart.config.options.scales = axes
    chart.update()
  }

  private def temperatureDatasetConfig(temperatures: js.Array[Double | Null]): ChartDataset = {
    new ChartDataset(
      label = "Temperature",
      typ = "bar",
      yAxisID = "yMain",
      data = temperatures
    ).updateDynamic(
      "spanGaps" -> true
    )
  }

  private def pressureDatasetConfig(pressures: js.Array[Double | Null]): ChartDataset = {
    new ChartDataset(
      label = "Pressure",
      typ = "line",
      yAxisID = "yPressure",
      data = pressures
    ).updateDynamic(
      "spanGaps" -> true
    )
  }

  private def scalesConfig(
    temperatures: List[Double],
    pressures: List[Double]
  ): js.Dictionary[ChartAxis] = {
    val xAxis = new ChartAxis()
      .updateDynamic(
        "type" -> "category",
        "grid" -> js.Dictionary(
          "display" -> false
        )
      )
    val scales = js.Dictionary(
      "x" -> xAxis,
      "yMain" -> yMainScale(temperatures)
    )

    yPressureScale(pressures).foreach { scale =>
      scales.update("yPressure", scale)
    }

    scales
  }

  private def yMainScale(temperatures: List[Double]): ChartAxis = {
    val stepSize = 5
    val range = fixedScaleRange(
      step = stepSize,
      desiredNumSteps = 7,
      dataValues = temperatures,
      forceMin = if (temperatures.forall(_ > 0)) Some(0) else None
    )
    new ChartAxis(
      position = "left",
      min = range.map(_._1).orUndefined,
      max = range.map(_._2).orUndefined
    ).updateDynamic(
      "ticks" -> js.Dictionary(
        //"count" -> 7,
        "stepSize" -> stepSize
      ),
      "grid" -> js.Dictionary(
        "display" -> false
      )
    )
  }

  private def yPressureScale(pressures: List[Double]): Option[ChartAxis] = {
    val pressureStepSize = 0.1
    fixedScaleRange(
      step = pressureStepSize,
      desiredNumSteps = 8,
      dataValues = pressures
    ).map { (min, max) =>
      new ChartAxis(
        position = "right",
        min = min,
        max = max
      ).updateDynamic(
        "ticks" -> js.Dictionary(
          "stepSize" -> pressureStepSize,
          "callback" -> (((value: Double) => f"$value%.1f"): js.Function1[Double, String])
        ),
        "grid" -> js.Dictionary(
          "display" -> false
        )
      )
    }
  }

  /** We want vertical axes to have consistent range (max - min value),
    * so that it's easier for the user to judge the difference between
    * the values for different cities on the chart. We also want to
    * snap the min and max to certain steps.
    * */
  private def fixedScaleRange(
    step: Double,
    desiredNumSteps: Int,
    dataValues: List[Double],
    forceMin: Option[Double] = None
  ): Option[(Double, Double)] = {
    for {
      minValue <- dataValues.minOption
      maxValue <- dataValues.maxOption
    } yield {
      val desiredRange = desiredNumSteps * step
      val scaleMin = forceMin.getOrElse {
        val mean = (minValue + maxValue) / 2
        Math.floor((mean - desiredRange / 2) / step) * step
      }
      val scaleMax = scaleMin + desiredRange
      (scaleMin, scaleMax)
    }
  }
}
