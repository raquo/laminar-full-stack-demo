package com.raquo.app.weather

import com.raquo.utils.Utils.*
import com.raquo.weather.GradientReport
import org.scalajs.dom
import vendor.chartjs.{Chart, ChartAxis, ChartConfig, ChartConfigOptions, ChartData, ChartDataset}

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

// BEGIN[wind-gradient]
object WeatherGradientChart {

  def makeChart(
    canvasElement: dom.html.Canvas
  ): Chart = {
    // Note: most data and configuration is updated when the graph data is received

    val chart = new Chart(
      canvas = canvasElement,
      config = new ChartConfig(
        data = new ChartData(),
        options = new ChartConfigOptions(
          animation = false,
          plugins = js.Dictionary(
            "legend" -> js.Dynamic.literal(
              "display" -> true,
              "position" -> "bottom",
              "labels" -> js.Dynamic.literal(
                "boxHeight" -> 0,
              )
            ),
            "datalabels" -> js.Dynamic.literal(
              "font" -> js.Dynamic.literal(
                "weight" -> "bold"
              )
            )
          ),
          // This makes the chart respond to match the size of its
          // parent container (-graphbox in our case).
          // See https://www.chartjs.org/docs/latest/configuration/responsive.html
          maintainAspectRatio = false
        )
      )
    )

    chart
  }

  def updateChart(chart: Chart, selectedDay: Option[String], report: GradientReport): Unit = {
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
      typ = "line",
      yAxisID = "yMain",
      data = temperatures,
      borderColor = "#0000f0",
      pointBackgroundColor = "#0000d0",
      pointBorderWidth = 0,
      pointRadius = 4,
      pointHoverRadius = 5,
      pointHoverBorderWidth = 0
    ).updateDynamic(
      "spanGaps" -> true,
      "datalabels" -> js.Dynamic.literal(
        "align" -> "top",
        "color" -> "#000080",
        "formatter" -> (((value: Double, ctx: js.Dynamic) => {
          Math.round(value).toString + "°"
        }): js.Function2[Double, js.Dynamic, Any])
      )
    )
  }

  private def pressureDatasetConfig(pressures: js.Array[Double | Null]): ChartDataset = {
    new ChartDataset(
      label = "Pressure",
      typ = "line",
      yAxisID = "yPressure",
      data = pressures,
      borderColor = "#f00000",
      pointBackgroundColor = "#d00000",
      pointBorderWidth = 0,
      pointRadius = 4,
      pointHoverRadius = 5,
      pointHoverBorderWidth = 0
    ).updateDynamic(
      "spanGaps" -> true,
      "datalabels" -> js.Dynamic.literal(
        "align" -> "top",
        "color" -> "#800000"
      )
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
        ),
        "ticks" -> js.Dynamic.literal(
          "minRotation" -> 90,
          "maxRotation" -> 90,
        ),
        "offset" -> true
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
        max = max + pressureStepSize // allow extra space for data point labels
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
// END[wind-gradient]
