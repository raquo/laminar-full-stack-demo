package vendor.highlightjs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait Language extends js.Object {
  val name: String = js.native
}

object Language {

  @js.native
  @JSImport("highlight.js/lib/languages/scala", JSImport.Default)
  object Scala extends Language

  @js.native
  @JSImport("highlight.js/lib/languages/javascript", JSImport.Default)
  object Javascript extends Language

  @js.native
  @JSImport("highlight.js/lib/languages/less", JSImport.Default)
  object Less extends Language

  @js.native
  @JSImport("highlight.js/lib/languages/css", JSImport.Default)
  object Css extends Language

  @js.native
  @JSImport("highlight.js/lib/languages/xml", JSImport.Default)
  object Html extends Language

}
