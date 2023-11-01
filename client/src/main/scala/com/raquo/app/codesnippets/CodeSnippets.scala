package com.raquo.app.codesnippets

import com.raquo.app.codesnippets.generated.GeneratedSnippets
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect

object CodeSnippets {

  JSImportSideEffect("@find/**/CodeSnippets.less")

  def apply(
    snippets: GeneratedSnippets.type => List[CodeSnippet],
    caption: String | Unit = (),
    asParagraph: Boolean = false,
    startExpanded: CodeSnippet => Boolean = _ => true
  ): HtmlElement = {
    val _snippets = snippets(GeneratedSnippets)
    val tag = if (asParagraph) p else div // Example of using dynamic tag
    tag(
      cls("CodeSnippets"),
      (caption, asParagraph) match {
        case (str: String, _) => str
        case ((), true) => emptyNode
        case ((), false) => if (_snippets.length == 1) "Source:" else "Sources:"
      },
      _snippets.map { snippet =>
        CodeSnippet.render(snippet, startExpanded(snippet))
      }
    )
  }

}
