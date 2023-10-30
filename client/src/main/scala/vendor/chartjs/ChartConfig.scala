package vendor.chartjs

import com.raquo.utils.DynamicJsObject

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

// #TODO Isn't there a similar union type built-in?
type Number = Double | Float | Int | Short | Byte

// Chart.js accepts the config in this format, but the Chart
// object also exposes its current config, and lets you update
// the chart's config by mutating it. That's why all the members
// are var-s.

// WARNING: chart.config is typed to return an instance of
// ChartConfig, but it's actually returning a plain JS object
// that follows the same structure. THEREFORE, you should not
// add any method implementations to these classes – basically
// treat these classes as @js.native. You can of course go ahead
// and define actual @js.native traits with the same structure,
// but you would need to keep the classes too (because you can
// not instantiate js.native traits), and the though of such
// redundancy is killing me, so I'm not doing it. But if you
// want more precise types, you can do it.

/**
  * @param plugins Inline plugins for this chart.
  *                - See [[https://www.chartjs.org/docs/latest/configuration/ Chart.js docs]]
  *                  and [[https://www.chartjs.org/docs/latest/developers/plugins.html API]]
  */
class ChartConfig(
  @JSName("type")
  var typ: String = null,
  var data: ChartData,
  var options: ChartConfigOptions = ChartConfigOptions(),
  var plugins: js.Dictionary[js.Object] = js.Dictionary()
) extends DynamicJsObject


/**
  * @param labels X axis labels
  */
class ChartData(
  var labels: js.Array[String] | Unit = js.undefined,
  var datasets: js.Array[ChartDataset] | Unit = js.undefined
) extends DynamicJsObject


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
  var label: String,
  @JSName("type")
  var typ: String | Unit = js.undefined,
  var data: js.Array[_],
  var backgroundColor: String | Unit = js.undefined,
  var borderColor: String | Unit = js.undefined,
  var pointBackgroundColor: String | Unit = js.undefined,
  var pointBorderColor: String | Unit = js.undefined,
  var pointBorderWidth: Number | Unit = js.undefined,
  var pointRadius: Number | Unit = js.undefined,
  var pointHoverBorderWidth: Number | Unit = js.undefined,
  var pointHoverRadius: Number | Unit = js.undefined,
  var parsing: js.Dictionary[String] | Boolean | Unit = js.undefined,
  var xAxisID: String | Unit = js.undefined,
  var yAxisID: String | Unit = js.undefined,
) extends DynamicJsObject

/**
  * @param parsing   How to parse the dataset.
  *                  - The parsing can be disabled by specifying `parsing: false`.
  *                  - If parsing is disabled, data must be sorted and in the formats the associated
  *                    chart type and scales use internally.
  *
  *                  Example data: `[{id: 'Sales', nested: {value: 1500}}, {id: 'Purchases', nested: {value: 500}}]`
  *                  And its parsing config for bar chart: `js.Dictionary("xAxisKey" -> "id", "yAxisKey" -> "nested.value")`
  *
  * @param scales    Define the scales: scaleId => { type: "logarithmic", position: "right" }
  *                  - Chart.js can infer axis orientation (X or Y) from the position.
  *                  - Use axis ids on datasets
  *
  * @param animation set false to disable animation
  *
  * @param plugins   Requires a pluginId => pluginConfig dictionary.
  *                  - Pass `false` to disable a specific plugin, or all plugins.
  *                  - See [[https://www.chartjs.org/docs/latest/developers/plugins.html Chart.js docs]]
  *                    and [[https://www.chartjs.org/docs/latest/api/interfaces/Plugin.html API]]
  */
class ChartConfigOptions(
  var parsing: js.Dictionary[String] | Boolean | Unit = js.undefined,
  var scales: js.Dictionary[ChartAxis] | Unit = js.undefined,
  var animation: ChartAnimation | Boolean | Unit = js.undefined,
  var plugins: js.Dictionary[js.Object | Boolean] | Boolean | Unit = js.undefined
) extends DynamicJsObject

/** @param typ      "linear" (?) | "time" | "logarithmic" | "r" etc.
  * @param position left | right | top | bottom | ?
  *
  *                 [[https://www.chartjs.org/docs/latest/axes/ Axes docs]]
  */
class ChartAxis(
  @JSName("type")
  var typ: String | Unit = js.undefined,
  var position: String | Unit = js.undefined,
  var beginAtZero: Boolean | Unit = js.undefined,
  var min: Number | Unit = js.undefined,
  var max: Number | Unit = js.undefined,
  var suggestedMin: Number | Unit = js.undefined,
  var suggestedMax: Number | Unit = js.undefined
) extends DynamicJsObject

/**
  * @param duration in milliseconds
  * @param easing   "linear" | "easeInQuad" | "easeOutSine" etc. See [[https://www.chartjs.org/docs/latest/configuration/animations.html#easing full list]]
  * @param delay    delay before the animation starts, in milliseconds
  * @param loop     if true, animation will loop endlessly
  */
class ChartAnimation(
  var duration: Number | Unit = js.undefined,
  var easing: String | Unit = js.undefined,
  var delay: Number | Unit = js.undefined,
  var loop: Boolean | Unit = js.undefined
) extends DynamicJsObject
