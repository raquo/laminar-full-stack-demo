package com.raquo.app

import com.raquo.app.JsRouter.*
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect
import org.scalajs.dom
import vendor.highlightjs.hljs

object JsApp {

  // Load languages that we'll need for code snippets in Highlight.js
  hljs.registerLanguage(_.Scala, _.Scala)
  hljs.registerLanguage(_.Javascript, _.Javascript)
  hljs.registerLanguage(_.Less, _.Less)
  //hljs.registerLanguage(_.Css, _.Css)
  //hljs.registerLanguage(_.Html, _.Html)

  // Find and import the LESS (CSS) file for this component. See globResolverPlugin and importSideEffectPlugin
  JSImportSideEffect("@find/**/JsApp.less")

  // This method is the entry point of your JS app.
  // It is recognized by its name and type signature,
  // do not rename it.
  def main(args: Array[String]): Unit = {
    // Scala.js outputs to the browser dev console, not the sbt session
    // Always have the browser dev console open when developing web UIs.
    println("-- Scala.js app start --")

    // Find the div to render the app into. It's defined in index.html
    lazy val container = dom.document.getElementById("root")

    lazy val appElement = {
      div(
        cls := "JsApp",
        div(
          cls := "-content",
          child.maybe <-- JsRouter.currentPageSignal.map {
            case HomePage => None
            case _ => Some(h3(a(navigateTo(HomePage), "Back to home")))
          },
          // #Exercise for advanced readers: JsRouter.currentPageSignal emits
          // very rarely (only when user navigates to another page). However,
          // imagine if it was emitting various pages 1000 times per second.
          // Your task: learn about the `split` operator to understand what
          // is inefficient about this .map in such a scenario, and fix the
          // inefficiency using the `splitOne` version of that operator.
          child <-- JsRouter.currentPageSignal.map {
            case page: TitledPage => h1(page.title)
            case _ => emptyNode
          },
          child <-- views
        )
      )
    }
    render(container, appElement)
  }
}
