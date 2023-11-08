package vendor.shoelace.components

import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.*
import com.raquo.laminar.defs.styles.{traits as s, units as u}
import org.scalajs.dom
import vendor.shoelace.{CommonKeys, HasGetForm, WebComponent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// BEGIN[shoelace/components]
/** [[https://shoelace.style/components/switch Shoelace docs]] */
object Switch extends WebComponent("sl-switch") { self =>

  @JSImport("@shoelace-style/shoelace/dist/components/switch/switch.js", JSImport.Namespace)
  @js.native protected object RawImport extends js.Object

  type Ref = dom.HTMLInputElement with HasGetForm


  // -- Events --

  export CommonKeys.{onChange, onInput, onBlur, onFocus, onInvalid}


  // -- Props --

  export L.{nameAttr => name, value, disabled, required, checked, defaultChecked, formId}


  // -- Slots --

  @inline def noSlots: Unit = ()


  // -- CSS Custom Properties

  // BEGIN[shoelace/css-custom-properties]
  lazy val width: StyleProp[String] with s.Auto with u.Length[DSP, Int] = lengthAutoStyle("--width")

  lazy val height: StyleProp[String] with s.Auto with u.Length[DSP, Int] = lengthAutoStyle("--height")

  lazy val thumbSize: StyleProp[String] with s.Auto with u.Length[DSP, Int] = lengthAutoStyle("--thumb-size")
  // END[shoelace/css-custom-properties]

  // -- CSS Parts --

  object parts {

    /** The component’s base wrapper. */
    val base: String = "base"

    /** The control that houses the switch’s thumb. */
    val control: String = "control"

    /** The switch’s thumb. */
    val thumb: String = "thumb"

    /** The switch’s label. */
    val label: String = "label"
  }

}
// END[shoelace/components]
