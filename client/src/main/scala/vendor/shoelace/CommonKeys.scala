package vendor.shoelace

import com.raquo.laminar.api.L.*
import com.raquo.laminar.keys.{EventProp, HtmlProp} // #TODO sort out prop imports
import com.raquo.laminar.codecs.*
import org.scalajs.dom

// BEGIN[shoelace/components]
/** Typical events / properties / etc. defined on Shoelace web components.
  * We selectively export them from this object into individual components
  * that define them.
  */
object CommonKeys extends CommonTypes {

  /** Emitted when the control’s "state" changes, similar to the browser's `change` event. */
  lazy val onChange: EventProp[dom.Event] = eventProp("sl-change")

  /** Emitted when the control receives input, similar to the browser's `input` event */
  lazy val onInput: EventProp[dom.Event] = eventProp("sl-input")

  /** Emitted when the component loses focus. */
  lazy val onBlur: EventProp[dom.Event] = eventProp("sl-blur")

  /** Emitted when the component obtains focus. */
  lazy val onFocus: EventProp[dom.Event] = eventProp("sl-focus")

  /** Emitted when the form control has been checked for validity and its constraints aren’t satisfied. */
  lazy val onInvalid: EventProp[dom.Event] = eventProp("sl-invalid")
  
  object size extends HtmlPropOf[String]("size", StringAsIsCodec) {

    lazy val small: PropSetterOf[String] = size("small")

    lazy val medium: PropSetterOf[String] = size("medium")

    lazy val large: PropSetterOf[String] = size("large")
  }

  object variant extends HtmlProp[String, String]("variant", StringAsIsCodec) {

    lazy val default: PropSetterOf[String] = variant("default")

    lazy val primary: PropSetterOf[String] = variant("primary")

    lazy val success: PropSetterOf[String] = variant("success")

    lazy val neutral: PropSetterOf[String] = variant("neutral")

    lazy val warning: PropSetterOf[String] = variant("warning")

    lazy val danger: PropSetterOf[String] = variant("danger")
  }
}
// END[shoelace/components]
