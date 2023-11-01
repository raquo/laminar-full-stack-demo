package vendor.chartjs

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// This Scala.js import:
//
//     @js.native
//     @JSImport("chart.js")
//     class Chart() extends js.Object
//
// is equivalent to the following JS import:
//
//     import { Chart } from "chart.js"
//
// where `Chart` is implied from the Scala class/object name that follows.
//
// If instead you wanted to use a different name in Scala for the imported class:
//
//     import { Chart => JsChart } from "chart.js"
//
// you would instead say:
//
//     @js.native
//     @JSImport("chart.js", "Chart")
//     class JsChart() extends js.Object
//
// See https://www.scala-js.org/doc/interoperability/facade-types.html for more details.

// BEGIN[wind-gradient]
@js.native
@JSImport("chart.js")
class Chart(
  val canvas: dom.HTMLCanvasElement | dom.CanvasRenderingContext2D,
  val config: ChartConfig
) extends js.Object {
  
  /** Call this after mutating chart config, for it to take effect. */
  def update(): Unit = js.native

  def destroy(): Unit = js.native
}

@js.native
@JSImport("chart.js")
object Chart extends js.Object {
  
  def defaults: ChartConfigOptions = js.native
  
  // Can accept: chart.js controllers, elements, plugins
  def register(components: js.Object*): Unit = js.native

  def unregister(components: js.Object*): Unit = js.native
}
// END[wind-gradient]

@js.native
@JSImport("chart.js")
object Colors extends js.Object

@js.native
@JSImport("chart.js")
object BarController extends js.Object

// BEGIN[wind-gradient]
@js.native
@JSImport("chart.js")
object LineController extends js.Object

@js.native
@JSImport("chart.js")
object CategoryScale extends js.Object

@js.native
@JSImport("chart.js")
object LinearScale extends js.Object
// END[wind-gradient]

@js.native
@JSImport("chart.js")
object BarElement extends js.Object

@js.native
@JSImport("chart.js")
object LineElement extends js.Object

@js.native
@JSImport("chart.js")
object PointElement extends js.Object

@js.native
@JSImport("chart.js")
object Legend extends js.Object
