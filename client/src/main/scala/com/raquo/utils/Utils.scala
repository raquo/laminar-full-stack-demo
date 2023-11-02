package com.raquo.utils

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

object Utils {

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
