package vendor.shoelace

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.RegExp
import scala.scalajs.js.annotation.JSImport

object Shoelace {

  @js.native
  @JSImport("@shoelace-style/shoelace/dist/utilities/base-path.js")
  def setBasePath(path: String): Unit = js.native

  @js.native
  @JSImport("@shoelace-style/shoelace/dist/utilities/base-path.js")
  def getBasePath(path: String): String = js.native

  @js.native
  @JSImport("@shoelace-style/shoelace/dist/utilities/icon-library.js")
  def registerIconLibrary(name: String, config: IconLibraryConfig): Unit = js.native

  /** [[https://shoelace.style/components/icon Shoelace docs]] */
  class IconLibraryConfig(
    val resolver: js.Function1[String, String],
    val mutator: js.Function1[dom.SVGElement, Unit] | Unit = (),
    val spriteSheet: Boolean = false
  ) extends js.Object

  /** The default icon library contains over 1,300 icons courtesy of the
    * [[https://icons.getbootstrap.com/ Bootstrap Icons]] project.
    * These are the icons that display when you use <sl-icon> without the library attribute.
    *
    * Call this method to have these icons resolve elsewhere, or to use a different default icon library.
    */
  def setDefaultIconLibrary(config: IconLibraryConfig): Unit = registerIconLibrary("default", config)

  /** Config for the [[https://tabler-icons.io/ Tabler Icons]] library using the jsDelivr CDN.
    * This library features over 1,950 open source icons.
    *
    * Icons in this library are licensed under the [[https://github.com/tabler/tabler-icons/blob/master/LICENSE MIT License]].
    */
  def tablerIconLibraryConfig(version: String = "2.40.0"): IconLibraryConfig = IconLibraryConfig(
    resolver = name => s"https://cdn.jsdelivr.net/npm/@tabler/icons@${version}/icons/${name}.svg"
  )


  /** Config for the [[https://fontawesome.com/search?m=free&o=r]] Font Awesome Free icons using the jsDeliver CDN.
    * This library has three variations: regular (far-*), solid (fas-*), and brands (fab-*).
    *
    * Icons in this library are licensed under the [[https://github.com/FortAwesome/Font-Awesome/blob/master/LICENSE.txt Font Awesome Free License]].
    */
  def fontAwesomeIconLibraryConfig(version: String = "6.4.2"): IconLibraryConfig = {
    val pattern = new RegExp("/^ fa[rbs]-/")
    IconLibraryConfig(
      resolver = name => {
        val filename = if (pattern.test(name)) name.substring(4) else name
        val folder = if (name.startsWith("fas-")) "solid" else if (name.startsWith("fab-")) "brands" else "regular"
        s"https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@${version}/svgs/${folder}/${filename}.svg"
      },
      mutator = svg => svg.setAttribute("fill", "currentColor")
    )
  }

  // See configs for several other icon libraries at https://shoelace.style/components/icon

}
