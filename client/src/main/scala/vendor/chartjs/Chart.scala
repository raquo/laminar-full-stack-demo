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
  
  // Can accept: ..., plugins
  def register(components: js.Object*): Unit = js.native

  def unregister(components: js.Object*): Unit = js.native
}

@js.native
@JSImport("chart.js", "Colors")
object Colors extends js.Object

@js.native
@JSImport("chart.js", "BarController")
object BarController extends js.Object

@js.native
@JSImport("chart.js", "LineController")
object LineController extends js.Object

@js.native
@JSImport("chart.js", "CategoryScale")
object CategoryScale extends js.Object

@js.native
@JSImport("chart.js", "LinearScale")
object LinearScale extends js.Object

@js.native
@JSImport("chart.js", "BarElement")
object BarElement extends js.Object

@js.native
@JSImport("chart.js", "LineElement")
object LineElement extends js.Object

@js.native
@JSImport("chart.js", "PointElement")
object PointElement extends js.Object

@js.native
@JSImport("chart.js", "Legend")
object Legend extends js.Object