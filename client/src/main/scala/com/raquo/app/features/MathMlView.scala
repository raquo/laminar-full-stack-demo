package com.raquo.app.features

import com.raquo.app.codesnippets.CodeSnippets
// BEGIN[mathml/import]
import com.raquo.laminar.api.{*, given}          // Standard Laminar things and implicits
import com.raquo.laminar.api.L.mathml.*          // MathML tags, attrs, and symbols
import com.raquo.laminar.api.features.unitArrows // Direct .update syntax
// END[mathml/import]
import com.raquo.laminar.domapi.DomApi

object MathMlView {

  def apply(): L.HtmlElement = {
    val resultNameVar = L.Var("x")
    L.div(
      L.h1("MathML"),
      L.p("Below you should see a quadratic equation:"),
      L.p(mathMlEquation(resultNameVar)),
      L.p("Click on the equals sign to change between ", L.code("x"), " and ", L.code("y"), "."),
      L.p("This uses standard Laminar features: ", L.code("onClick -->"), " and ", L.code("text <--"), "."),
      CodeSnippets(
        _.`mathml/equation`,
        caption = "MathML equation source:",
        startExpanded = _ => false
      ),
      L.p("In this snippet, we chose to import all MathML tags and attrs for convenience. To avoid name collisions with standard Laminar APIs, we access the latter through the ", L.code("L."), " namespace."),
      CodeSnippets(
        _.`mathml/import`,
        caption = "Imports"
      )
    )
  }

  // BEGIN[mathml/equation]
  def mathMlEquation(resultNameS: L.Var[String]): L.MathMlElement = {
    mathTag(
      xmlns(DomApi.mathmlNamespaceUri), // Optional if rendering MathML in a modern browser
      display("block"),
      mrow(
        mi(L.text <-- resultNameS),
        mo(
          L.onClick --> resultNameS.update {
            case "x" => "y"
            case _ => "x"
          },
          symbols.equalsSign
        ),
        mfrac(
          mrow(
            mrow(
              mo(symbols.minus),
              mi("b"),
            ),
            mo(symbols.plusMinus),
            msqrt(
              mrow(
                msup(
                  mi("b"),
                  mn(2)
                ),
                mo(symbols.minus),
                mrow(
                  mn(4),
                  mo(symbols.invisibleTimes),
                  mi("a"),
                  mo(symbols.invisibleTimes),
                  mi("c")
                )
              )
            )
          ),
          mrow(
            mn(2),
            mo(symbols.invisibleTimes),
            mi("a")
          )
        )
      )
    )
  }
  // END[mathml/equation]
}
