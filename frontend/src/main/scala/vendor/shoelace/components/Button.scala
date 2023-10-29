package vendor.shoelace.components

import com.raquo.laminar.codecs.*
import com.raquo.laminar.keys.HtmlProp
import com.raquo.laminar.tags.HtmlTag
import com.raquo.utils.JSImportSideEffect
import org.scalajs.dom
import vendor.shoelace.{Shoelace, Slot, WebComponent}
import com.raquo.laminar.api.L.*
import com.raquo.laminar.modifiers.KeySetter
import com.raquo.laminar.nodes.ReactiveHtmlElement
import com.raquo.utils.Utils.HtmlModifier

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

/** [[https://shoelace.style/components/button Shoelace docs]] */
object Button extends WebComponent("sl-button") { self =>

  JSImportSideEffect("@shoelace-style/shoelace/dist/components/button/button.js")

  @js.native
  trait RawElement extends js.Object {

    /** Gets the associated form, if one exists. */
    def getForm(): dom.HTMLFormElement | Null
  }

  type Ref = dom.HTMLButtonElement with RawElement


  // -- Events --

  lazy val onBlur: EventProp[dom.Event] = eventProp("sl-blur")

  lazy val onFocus: EventProp[dom.Event] = eventProp("sl-focus")

  /** Emitted when the form control has been checked for validity and its constraints aren’t satisfied. */
  lazy val onInvalid: EventProp[dom.Event] = eventProp("sl-invalid")


  // -- Props --

  object variant extends HtmlPropOf[String]("variant", StringAsIsCodec) {

    lazy val default: PropSetter[String] = variant("default")

    lazy val primary: PropSetter[String] = variant("primary")

    lazy val success: PropSetter[String] = variant("success")

    lazy val neutral: PropSetter[String] = variant("neutral")

    lazy val warning: PropSetter[String] = variant("warning")

    lazy val danger: PropSetter[String] = variant("danger")
  }

  object size extends HtmlPropOf[String]("size", StringAsIsCodec) {

    lazy val small: PropSetter[String] = size("small")

    lazy val medium: PropSetter[String] = size("medium")

    lazy val large: PropSetter[String] = size("large")
  }

  /** Draws the button with a caret. Used to indicate that the button triggers a dropdown menu or similar behavior. */
  lazy val caret: HtmlPropOf[Boolean] = boolProp("caret")

  lazy val disabled: HtmlPropOf[Boolean] = boolProp("disabled")

  /** Draws the button in a loading state. */
  lazy val loading: HtmlPropOf[Boolean] = boolProp("loading")

  /** Draws an outlined button. */
  lazy val outline: HtmlPropOf[Boolean] = boolProp("outline")

  /** Draws a pill-style button with rounded edges. */
  lazy val pill: HtmlPropOf[Boolean] = boolProp("pill")

  /** Draws a circular icon button. When this attribute is present, the button expects a single <sl-icon> in the default slot. */
  lazy val circle: HtmlPropOf[Boolean] = boolProp("circle")

  /** When set, the underlying button will be rendered as an <a> with this href instead of a <button>.
    * Warning: Note that in this case, our `Ref` type will be incorrect.
    */
  lazy val href: HtmlAttr[String] = stringAttr("href")

  /** Tells the browser where to open the link. Only used when href is present. */
  lazy val target: HtmlAttr[String] = stringAttr("target")

  /** When using href, this attribute will map to the underlying link’s rel attribute.
    * Unlike regular links, the default is noreferrer noopener to prevent security exploits.
    * However, if you’re using target to point to a specific tab/window, this will prevent
    * that from working correctly. You can remove or change the default value by setting the
    * attribute to an empty string or a value of your choice, respectively.
    */
  lazy val rel: HtmlAttr[String] = stringAttr("rel")

  /** Tells the browser to download the linked file as this filename. Only used when href is present. */
  lazy val download: HtmlAttr[String] = stringAttr("download")


  // -- Slots --

  object slots {

    lazy val prefix: Slot = Slot("prefix")

    lazy val suffix: Slot = Slot("suffix")
  }


  // -- CSS parts --

  object parts {

    /** The component’s base wrapper. */
    val base: String = "base"

    /** The container that wraps the prefix. */
    val prefix: String = "prefix"

    /** The button’s label. */
    val label: String = "label"

    /** The container that wraps the suffix. */
    val suffix: String = "suffix"

    /** The button’s caret icon, an <sl-icon> element. */
    val caret: String = "caret"

    /** The spinner that shows when the button is in the loading state. */
    val spinner: String = "spinner"
  }

}
