package com.raquo.app.weather

import com.raquo.airstream.core.Observable
import com.raquo.app.JsRouter.*
import com.raquo.app.jsonApiDecoder
import com.raquo.app.pages.WeatherGradientPage
import com.raquo.data.ApiResponse
import com.raquo.laminar.api.L.*
import com.raquo.utils.Url.*
import com.raquo.weather.{Gradient, GradientReport}
import vendor.chartjs.*

import scala.scalajs.js

object WeatherGradientView {

  // Chart.js requires you to load the necessary components this way
  // to avoid loading the entire library. It will also tell you which
  // components are needed if you forget.
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

    // #Note: def is important here, but hopefully not for long, looking to fix it in 17.0.0
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
    val gradientReportS = apiResponseS.collect {
      case ApiResponse.Result(report) => report
    }
    val maybeApiErrorS = apiResponseS.collect {
      case ApiResponse.Error(msg, _) => Some(msg)
      case _ => None
    }

    div(
      h1(child.text <-- gradientS.map(_.name + " gradient")),
      child.maybe <-- maybeApiErrorS.map(_.map(err => div(textAlign.center, span(cls("u-error"), err)))),
      child <-- gradientReportS.map { report =>
        import vendor.chartjs.*

        import scala.scalajs.js.JSConverters.*

        val cityNames = report.cities.toJSArray.map(_.name)

        val datasets = report.cities.map { city =>
          val maybeConditions = report.currentConditionsByCity.get(city.id)
          maybeConditions match {
            case Some(conditions) =>
              conditions.temperatureC.orNull
            case None =>
              null
          }
        }

        val temperatures = report.cities.map { city =>
          val maybeConditions = report.currentConditionsByCity.get(city.id)
          maybeConditions match {
            case Some(conditions) =>
              conditions.temperatureC.orNull: Double | Null
            case None =>
              null
          }
        }.toJSArray

        val pressures = report.cities.map { city =>
          val maybeConditions = report.currentConditionsByCity.get(city.id)
          maybeConditions match {
            case Some(conditions) =>
              conditions.pressureKPa.orNull: Double | Null
            case None =>
              null
          }
        }.toJSArray

        canvasTag(
          width := "500px",
          height := "200px",

          onMountUnmountCallbackWithState(
            mount = { nodeCtx =>
              Chart(
                canvas = nodeCtx.thisNode.ref,
                config = ChartConfig(
                  data = ChartData(
                    labels = cityNames,
                    datasets = js.Array(
                      ChartDataset(
                        label = "Temperature",
                        typ = "bar",
                        yAxisID = "yMain",
                        data = temperatures
                      ).updateDynamic(
                        "spanGaps" -> true
                      ),
                      ChartDataset(
                        label = "Pressure",
                        typ = "line",
                        yAxisID = "yPressure",
                        data = pressures
                      ).updateDynamic(
                        "spanGaps" -> true
                      )
                    )
                  ),
                  options = ChartConfigOptions(
                    scales = js.Dictionary(
                      "x" -> ChartAxis()
                        .updateDynamic(
                          "type" -> "category",
                          "grid" -> js.Dictionary(
                            "display" -> false
                          )
                        ),
                      "yMain" -> ChartAxis(
                        position = "left",
                        beginAtZero = true
                      ).updateDynamic(
                        "grid" -> js.Dictionary(
                          "display" -> false
                        )
                      ),
                      "yPressure" -> ChartAxis(
                        position = "right",
                        beginAtZero = false
                      ).updateDynamic(
                        "grid" -> js.Dictionary(
                          "display" -> false
                        )
                      )
                    )
                  )
                ),
              )
            },
            unmount = { (_, maybeChart) =>
              maybeChart.foreach(_.destroy())
            }
          )
        )
      }
    )
  }
}
