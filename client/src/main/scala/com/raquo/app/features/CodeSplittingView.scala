package com.raquo.app.features

import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.api.features.unitArrows

object CodeSplittingView {

  def apply(): HtmlElement = {
    div(
      h1("Code Splitting"),
      p("As your application grows, it inevitably acquires functionality that most users need only occasionally. Wouldn't it be nice if the browser didn't have to download all that code until it's actually needed?"),
      p("The mechanism used to achieve this in JS is called ", a("dynamic imports", href("https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/import")), ", and in Scala.js it is available through the ", a("js.dynamicImport", href("https://www.scala-js.org/doc/project/module.html#dynamic-imports")), " magic method. You can use that directly, but we also provide convenient ", code("dynamicImport"), " operators on streams and signals."), //
      {
        // BEGIN[code-splitting]
        val triggerCountVar = Var(0)
        val triggerCountS: EventStream[Int] = triggerCountVar.signal.changes // excludes the initial `0` value
        div(
          p(
            button(
              onClick --> triggerCountVar.update(_ + 1),
              "Click to lazy-load some content"
            ),
          ),
          child <-- triggerCountS.dynamicImport { triggerCount =>
            println(s"Lazy loading ${triggerCount}")
            LazyLoadedView(triggerCount)
          }
        )
        // END[code-splitting]
      },
      CodeSnippets(_.`code-splitting`)
    )
  }

}
