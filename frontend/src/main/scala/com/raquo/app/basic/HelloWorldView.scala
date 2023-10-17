package com.raquo.app.basic

import com.raquo.laminar.api.L.{*, given}

object HelloWorldView {

  def apply(): HtmlElement = {
    // #Exercise for the reader:
    // What will change if we move nameVar to the
    // outside of the `apply` method, and why?
    // HINT: htrof dna kcab gnitagivan yrt
    val nameVar = Var(initial = "world")

    div(
      label("Your name: "),
      input(
        onMountFocus,
        placeholder := "Enter your name here",
        onInput.mapToValue --> nameVar
      ),
      p(
        "Hello, ",
        child.text <-- nameVar.signal.map(_.toUpperCase)
      )
    )
  }
}
