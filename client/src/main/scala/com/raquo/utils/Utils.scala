package com.raquo.utils

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

import scala.scalajs.js

object Utils {

  /** Marks the otherwise-unused import as "used" in Scala.js,
    * preventing dead code elimination.
    */
  def useImport(importedObject: js.Any): Unit = ()

  // #TODO: Change the HtmlMod type in Laminar to accept a type param?

  type HtmlModifier[-El <: dom.html.Element] = Modifier[ReactiveHtmlElement[El]]

  object HtmlModifier {

    type Base = Modifier[ReactiveHtmlElement.Base]
  }

  extension [A](a: A)
    def some: Some[A] = Some(a)

  // Eh maybe I should add something like it to Laminar
  extension [El <: ReactiveHtmlElement.Base](mod: Modifier[El])
    def when(cond: Boolean): Modifier[El] = {
      if (cond) mod else emptyMod
    }

}
