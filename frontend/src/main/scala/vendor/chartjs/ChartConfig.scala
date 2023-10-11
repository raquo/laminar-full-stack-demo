package vendor.chartjs

import com.raquo.utils.DynamicJsObject

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

/**
  * @param plugins Inline plugins for this chart.
  *                - See [[https://www.chartjs.org/docs/latest/configuration/ Chart.js docs]]
  *                  and [[https://www.chartjs.org/docs/latest/developers/plugins.html API]]
  */
class ChartConfig(
  @JSName("type")
  val typ: String = null,
  val data: ChartData,
  val options: ChartConfigOptions | Unit = js.undefined,
  val plugins: js.Array[js.Object] | Unit = js.undefined
) extends js.Object


/**
  * @param labels X axis labels
  */
class ChartData(
  val labels: js.Array[String] | Unit = js.undefined,
  val datasets: js.Array[ChartDataset]
) extends js.Object


/**
  * @param label   Caption for this dataset
  *
  * @param data    Array of records. Actual records can be in very different shapes. See Chart.js docs.
  *
  * @param parsing How to parse the dataset.
  *                - The parsing can be disabled by specifying `parsing: false`.
  *                - If parsing is disabled, data must be sorted and in the formats the associated
  *                  chart type and scales use internally.
  *
  *                Example data: `[{id: 'Sales', nested: {value: 1500}}, {id: 'Purchases', nested: {value: 500}}]`
  *                And its parsing config for bar chart: `js.Dictionary("xAxisKey" -> "id", "yAxisKey" -> "nested.value")`
  */
class ChartDataset(
  val label: String,
  @JSName("type")
  val typ: String | Unit = js.undefined,
  val data: js.Array[_],
  val parsing: js.Dictionary[String] | Boolean | Unit = js.undefined,
  val xAxisID: String | Unit = js.undefined,
  val yAxisID: String | Unit = js.undefined,
) extends DynamicJsObject

/**
  * @param parsing How to parse the dataset.
  *                - The parsing can be disabled by specifying `parsing: false`.
  *                - If parsing is disabled, data must be sorted and in the formats the associated
  *                  chart type and scales use internally.
  *
  *                Example data: `[{id: 'Sales', nested: {value: 1500}}, {id: 'Purchases', nested: {value: 500}}]`
  *                And its parsing config for bar chart: `js.Dictionary("xAxisKey" -> "id", "yAxisKey" -> "nested.value")`
  *
  * @param scales  Define the scales: scaleId => { type: "logarithmic", position: "right" }
  *                 - Chart.js can infer axis orientation (X or Y) from the position.
  *                 - Use axis ids on datasets
  *
  * @param plugins Requires a pluginId => pluginConfig dictionary.
  *                - Pass `false` to disable a specific plugin, or all plugins.
  *                - See [[https://www.chartjs.org/docs/latest/developers/plugins.html Chart.js docs]]
  *                  and [[https://www.chartjs.org/docs/latest/api/interfaces/Plugin.html API]]
  */
class ChartConfigOptions(
  val parsing: js.Dictionary[String] | Boolean | Unit = js.undefined,
  val scales: js.Dictionary[ChartAxis] | Unit = js.undefined,
  val plugins: js.Dictionary[js.Object | Boolean] | Boolean | Unit = js.undefined
) extends js.Object

/** @param typ      linear (?) | time | logarithmic | r |
  * @param position left | right | top | bottom | ?
  *
  * [[https://www.chartjs.org/docs/latest/axes/ Axes docs]]
  */
class ChartAxis(
  @JSName("type")
  val typ: String | Unit = js.undefined,
  val position: String | Unit = js.undefined,
  val beginAtZero: Boolean | Unit = js.undefined
) extends DynamicJsObject
