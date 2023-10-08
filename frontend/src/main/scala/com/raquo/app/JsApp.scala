package com.raquo.app

import com.raquo.app.JsRouter.*
import com.raquo.app.weather.WeatherGradientView
import com.raquo.weather.Gradient
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom
import com.raquo.waypoint.*

import scala.scalajs.js

object JsApp {

  // This method is the entry point of your JS app.
  // It is recognized by its name and type signature,
  // do not rename it.
  def main(args: Array[String]): Unit = {
    println("-- Scala.js app start --")

    // Find the div to render the app into. It's defined in index.html
    lazy val container = dom.document.getElementById("root")

    lazy val appElement = {
      div(
        child.maybe <-- JsRouter.currentPageSignal.map {
          case HomePage => None
          case _ => Some(h3(a(navigateTo(HomePage), "Back to home")))
        },
        child <-- selectedAppSignal
      )
    }
    
    render(container, appElement)
  }


  private val selectedAppSignal = SplitRender(JsRouter.currentPageSignal)
    .collectStatic(HomePage)(renderHomePage())
    .collectSignal[WeatherGradientPage](WeatherGradientView(_))
    .signal

  private def renderHomePage(): HtmlElement = {
    div(
      h1("Laminar Examples"),
      ul(
        fontSize := "120%",
        lineHeight := "2em",
        listStyleType.none,
        paddingLeft := "0px",
        linkPages.map { (caption, page) =>
          li(a(navigateTo(page), caption))
        }
      )
    )
  }

  val linkPages: List[(String, Page)] = (
    Gradient.values.toList.map(gradient => (gradient.name, WeatherGradientPage(gradient.id))) ++ List()

    //DuckCounterPage,
    //TodoMvcPage,
    //AjaxTesterPage,
    //FetchTesterPage,
    //WebComponentsPage,
    //SvgContainerPage
  )







  def registerComponents(): Unit = {
    import vendor.chartjs.*
    Chart.register(
      BarController,
      BarElement,
      CategoryScale,
      LinearScale
    )
  }

  def renderDataGraph(): HtmlElement = {

    registerComponents()

    val clickBus: EventBus[Unit] = new EventBus
    val textToSendVar = Var("")

    div(
      h1("Weather demo+"),
      div(
        //child.text <-- FetchStream.post(
        //  url = "/api/do-thing",
        //  _.body(writeToString(SomeSharedData("hello", 2))),
        //  _.headers(
        //    "Content-Type" -> "application/json",
        //    "Accept" -> "text/plain"
        //  )
        //)
      ),
      renderDataGraph()
      )
    
    import vendor.chartjs.*
    canvasTag(
      width := "500px",
      height := "200px",

      onMountUnmountCallbackWithState(
        mount = { nodeCtx =>
          Chart(
            canvas = nodeCtx.thisNode.ref,
            config = ChartConfig(
              typ = "bar",
              data = ChartData(
                labels = js.Array("Red", "Blue", "Yellow", "Green", "Purple", "Orange"),
                datasets = js.Array(
                  ChartDataset(
                    label = "# of Votes",
                    data = js.Array(10, 19, 3, 5, 2, 3)
                  ).updateDynamic(
                    "backgroundColor" -> js.Array(
                      "rgb(255, 99, 132)",
                      "rgb(54, 162, 235)",
                      "rgb(255, 206, 86)",
                      "rgb(75, 192, 192)",
                      "rgb(153, 102, 255)",
                      "rgb(255, 159, 64)"
                    )
                  )
                )
              )
            )
          )
        },
        unmount = { (_, maybeChart) =>
          maybeChart.foreach(_.destroy())
        }
      )
    )
  }

  // --

  def registerComponentsST(): Unit = {
    import typings.chartJs.mod.*
    import typings.chartJs.distTypesIndexMod.ChartComponent
    Chart.register(
      js.Array(
        BarController.^,
        BarElement.^,
        CategoryScale.^,
        LinearScale.^
      ).map(_.asInstanceOf[ChartComponent])
    )
  }

  def renderDataGraphST(): HtmlElement = {
    import typings.chartJs.mod.*
    import typings.chartJs.distTypesIndexMod.{ChartComponent, ChartConfiguration, ChartData, ChartDataset, ChartOptions, ChartType}
    import typings.chartJs.chartJsStrings
    canvasTag(
      width := "500px",
      height := "200px",

      onMountUnmountCallbackWithState(
        mount = { nodeCtx =>
          val chartConfig = ChartConfiguration(
            `type` = chartJsStrings.bar,
            data = ChartData(
              datasets = js.Array(
                new js.Object {
                  val label = "# of Votes"
                  val data = js.Array(10, 19, 3, 5, 2, 3)
                  val backgroundColor = js.Array(
                    "rgb(255, 99, 132)",
                    "rgb(54, 162, 235)",
                    "rgb(255, 206, 86)",
                    "rgb(75, 192, 192)",
                    "rgb(153, 102, 255)",
                    "rgb(255, 159, 64)"
                  )
                }.asInstanceOf[ChartDataset[chartJsStrings.bar, _]]
              )
            ).setLabels(
              js.Array("Red", "Blue", "Yellow", "Green", "Purple", "Orange")
            )
          )

          Chart(nodeCtx.thisNode.ref, chartConfig)
        },
        unmount = { (_, maybeChart) =>
          maybeChart.foreach(_.destroy())
        }
      )
    )
  }

}
