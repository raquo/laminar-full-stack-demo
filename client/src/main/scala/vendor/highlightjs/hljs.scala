package vendor.highlightjs

import com.raquo.laminar.DomApi
import com.raquo.laminar.api.L.{code as codeTag, *}
import com.raquo.utils.Utils.useImport

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// BEGIN[codesnippets/highlight.js]

/** This is a simple but ergonomic wrapper for the Highlight.js library.
  * The actual JS interface is in `object raw`, but we make it a bit nicer to use.
  */
object hljs {

  @js.native @JSImport("highlight.js/styles/dark.min.css")
  private object Stylesheet extends js.Object

  useImport(Stylesheet)

  /** Marker trait (compatible with Scala 2), those are actually just strings */
  @js.native
  trait LanguageName extends js.Any

  /** [[https://highlightjs.readthedocs.io/en/latest/api.html Highlight.js API docs]] */
  @js.native
  @JSImport("highlight.js/lib/core", JSImport.Default)
  object raw extends js.Object {

    trait HighlightOptions extends js.Object {
      val language: LanguageName
      val ignoreIllegals: Boolean | Unit = js.undefined
    }

    @js.native
    trait HighlightResult extends js.Object {
      /** Raw input code that was formatted */
      val code: String
      val language: LanguageName
      /** HTML string with the formatted code */
      val value: String
    }

    def highlight(code: String, options: HighlightOptions): HighlightResult = js.native

    def registerLanguage(name: LanguageName, language: Language): Unit = js.native
  }

  // I like contextual APIs like this. Does not need extra imports,
  // and IDE autocomplete tells you what's available.
  def registerLanguage(
    name: LanguageName.type => LanguageName,
    language: Language.type => Language
  ): Unit = {
    raw.registerLanguage(name(LanguageName), language(Language))
  }

  /** Convenient highlight method that returns a Laminar element */
  def highlight(code: String, language: LanguageName, codeMods: Modifier[HtmlElement] = emptyMod): HtmlElement = {
    val _language = language
    val options = new raw.HighlightOptions {
      val language: LanguageName = _language
    }
    val result = raw.highlight(code, options)
    val jsElements = DomApi.unsafeParseHtmlStringIntoNodeArray(dangerousHtmlString = result.value)
    val element = codeTag(cls(s"hljs language-$language"), codeMods)
    jsElements.foreach(element.ref.appendChild)
    pre(element)
  }

  object LanguageName {
    val Scala: LanguageName = "scala".asInstanceOf[LanguageName]
    val Javascript: LanguageName = "javascript".asInstanceOf[LanguageName]
    val Less: LanguageName = "less".asInstanceOf[LanguageName]
    val Css: LanguageName = "css".asInstanceOf[LanguageName]
    val Html: LanguageName = "xml".asInstanceOf[LanguageName]
  }
}
// END[codesnippets/highlight.js]
