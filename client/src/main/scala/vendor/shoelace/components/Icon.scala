package vendor.shoelace.components

import com.raquo.laminar.api.L
import com.raquo.laminar.api.L.*
import com.raquo.laminar.defs.styles.traits as s
import com.raquo.utils.Utils.useImport
import org.scalajs.dom
import vendor.shoelace.WebComponent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport


// BEGIN[shoelace/components]
/** [[https://shoelace.style/components/icon Shoelace docs]] */
object Icon extends WebComponent("sl-icon") { self =>

  @JSImport("@shoelace-style/shoelace/dist/components/icon/icon.js", JSImport.Namespace)
  @js.native protected object RawImport extends js.Any

  type Ref = dom.HTMLButtonElement


  // -- Events --

  /** Emitted when the icon has loaded.
    * When using spriteSheet: true this will not emit.
    */
  lazy val onLoad: EventProp[dom.Event] = eventProp("sl-load")

  /** Emitted when the icon fails to load due to an error.
    * When using spriteSheet: true this will not emit.
    */
  lazy val onError: EventProp[dom.Event] = eventProp("sl-error")


  // -- Props --

  /** The name of the icon to draw. Available names depend on the icon library being used. */
  lazy val name: HtmlPropOf[String] = stringProp("name")

  // #TODO Is this XSS warning specific to Shoelace? https://developer.mozilla.org/en-US/docs/Web/SVG/SVG_as_an_Image
  //  Should we name this `unsafeSrc`?
  /** An external URL of an SVG file.
    *
    * Warning!!! Be sure you trust the content you are including, as it
    * will be executed as code, and can result in XSS attacks.
    */
  lazy val src: HtmlAttr[String] = stringAttr("src")

  /** An alternate description to use for assistive devices.
    * If omitted, the icon will be considered presentational
    * and ignored by assistive devices.
    */
  lazy val label: HtmlAttr[String] = stringAttr("label")

  /** The name of a registered custom icon library. */
  lazy val library: HtmlPropOf[String] = stringProp("library")

  /** Icons are sized relative to the current font size.
    * To change their size, set the font-size property
    * on the icon itself or on a parent element.
    */
  lazy val fontSize: StyleProp[String] with s.FontSize = L.fontSize


  // -- Slots --

  @inline def noSlots: Unit = ()


  // -- CSS Parts --

  object parts {

    /** The internal SVG element. */
    lazy val svg: String = "svg"

    /** The element generated when using spriteSheet: true */
    lazy val use: String = "use"
  }

}
// END[shoelace/components]
