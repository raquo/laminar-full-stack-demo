package com.raquo.app.codesnippets

import com.raquo.buildinfo.BuildInfo
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect
import vendor.highlightjs.hljs

import scala.scalajs.js

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

  JSImportSideEffect("@find/**/CodeSnippet.less")


  def render(snippet: CodeSnippet, startExpanded: Boolean = true): HtmlElement = {
    val isExpandedVar = Var(startExpanded)
    div(
      cls("CodeSnippet u-bleed"),
      div(
        cls("-sourceHeader u-unbleed"),
        onClick.mapTo(!isExpandedVar.now()) --> isExpandedVar,
        span(
          cls("-arrow"),
          child.text <-- isExpandedVar.signal.map(if (_) "▼" else "▶")
        ),
        span(
          cls("-filename"),
          snippet.fileName + " " + lineNumbers(snippet),
        ),
        a(
          cls("-githubLink"),
          href(githubUrl(snippet)),
          target("_blank"),
          "View on Github"
        )
      ),
      child <-- isExpandedVar.signal.map(if (_) {
        hljs.highlight(
          code = snippet.lines.mkString("\n"),
          language = snippet.fileLanguage
        )
      } else emptyNode)
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
