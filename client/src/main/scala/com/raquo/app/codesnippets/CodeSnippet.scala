package com.raquo.app.codesnippets

import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.Utils.useImport
import vendor.highlightjs.hljs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// BEGIN[codesnippets/object]
case class CodeSnippet(
  filePath: String,
  fileName: String,
  fileLanguage: hljs.LanguageName,
  startLineNumber: Int,
  endLineNumber: Int,
  key: String,
  lines: List[String]
)

object CodeSnippet {

  @js.native @JSImport("@find/**/CodeSnippet.less", JSImport.Namespace)
  private object Stylesheet extends js.Object

  useImport(Stylesheet)

  def render(snippet: CodeSnippet, startExpanded: Boolean = true): HtmlElement = {
    val isExpandedVar = Var(startExpanded)
    div(
      cls("CodeSnippet u-bleed"),
      div(
        cls("-sourceHeader u-unbleed"),
        onClick.mapTo(!isExpandedVar.now()) --> isExpandedVar,
        span(
          cls("-arrow"),
          text <-- isExpandedVar.signal.map(if (_) "▼" else "▶")
        ),
        span(
          cls("-filename"),
          snippet.fileName + " " + lineNumbers(snippet),
        ),
        a(
          cls("-githubLink"),
          // We need `stopPropagation` to prevent the link click event from bubbling,
          // triggering the parent div's onClick above. We give it an empty observer
          // just to get the subscription going, `stopPropagation` already contains
          // our desired side effect.
          // See https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Building_blocks/Events#event_bubbling
          onClick.stopPropagation --> Observer.empty,
          href(githubUrl(snippet)),
          target("_blank"),
          span(cls("u-hideOnMobile"), "View on "),
          "Github"
        )
      ),
      child(
        hljs.highlight(code = snippet.lines.mkString("\n"), snippet.fileLanguage)
      ) <-- isExpandedVar.signal
    )
  }

  def lineNumbers(snippet: CodeSnippet): String = {
    if (snippet.startLineNumber == 0 && snippet.endLineNumber == 0) {
      ""
    } else if (snippet.startLineNumber == snippet.endLineNumber) {
      s"L${snippet.startLineNumber}"
    } else {
      s"L${snippet.startLineNumber}-L${snippet.endLineNumber}"
    }
  }

  def githubUrl(snippet: CodeSnippet): String = {
    "https://github.com/raquo/laminar-full-stack-demo/blob/master/" + snippet.filePath + "#" + lineNumbers(snippet)
  }
}
// END[codesnippets/object]
