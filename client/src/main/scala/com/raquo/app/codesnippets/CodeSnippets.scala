package com.raquo.app.codesnippets

import com.raquo.app.codesnippets.generated.GeneratedSnippets
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect

object CodeSnippets {

  JSImportSideEffect("@find/**/CodeSnippets.less")

  def apply(
    snippets: GeneratedSnippets.type => List[CodeSnippet],
    caption: String | Unit = (),
    startExpanded: CodeSnippet => Boolean = _ => true
  ): HtmlElement = {
    val _snippets = snippets(GeneratedSnippets)
    div(
      cls("CodeSnippets"),
      caption match {
        case str: String => str
        case () => if (_snippets.length == 1) "Source:" else "Sources:"
      },
      _snippets.map { snippet =>
        CodeSnippet.render(snippet, startExpanded(snippet))
      }
    )
  }

}
