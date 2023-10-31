package com.raquo.app.codesnippets

import com.raquo.app.codesnippets.generated.GeneratedSnippets
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect

object CodeSnippets {

  JSImportSideEffect("@find/**/CodeSnippets.less")

  def apply(
    snippets: GeneratedSnippets.type => List[CodeSnippet],
    startExpanded: Boolean = true
  ): HtmlElement = {
    val _snippets = snippets(GeneratedSnippets)
    div(
      cls("CodeSnippets"),
      if (_snippets.length == 1) "Source:" else "Sources:",
      _snippets.map { snippet =>
        CodeSnippet.render(snippet, startExpanded)
      }
    )
  }

}
