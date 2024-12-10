package com.raquo.app.form

import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// BEGIN[form-state]
object FormStateView {

  // Find and import the LESS (CSS) file for this component.
  // See https://github.com/raquo/vite-plugin-glob-resolver
  // !! This is using non-standard compact syntax to import CSS
  // See https://github.com/raquo/vite-plugin-import-side-effect#compact-syntax
  @JSImport("@find/**/FormStateView.less")
  @js.native private def importStyle(): Unit = js.native

  importStyle()

  private val stateVar = Var(FormState())

  private val zipVar = stateVar.zoomLazy(_.zip)((state, zip) => state.copy(zip = zip))

  private val cityVar = stateVar.zoomLazy(_.city)((state, desc) => state.copy(city = desc))

  private val submitter = Observer[FormState] { state =>
    if (state.hasErrors) {
      stateVar.update(_.copy(showErrors = true))
    } else {
      dom.window.alert(s"Zip: ${state.zip}; Description: ${state.city}")
    }
  }

  def apply(): HtmlElement = {
    div(
      cls("FormStateView"),
      form(
        onSubmit
          .preventDefault
          .mapTo(stateVar.now()) --> submitter,

        renderInputRow(_.cityError)(
          label("City: "),
          input(
            controlled(
              value <-- cityVar,
              onInput.mapToValue --> cityVar
            )
          ),
          button(
            typ("button"), // "submit" is the default in HTML
            "Clear",
            onClick.mapTo("") --> cityVar
          )
        ),

        renderInputRow(_.zipError)(
          label("Zip code: "),
          input(
            cls("-zipCodeInput"),
            placeholder("12345"),
            controlled(
              value <-- zipVar,
              onInput.mapToValue.filter(_.forall(Character.isDigit)) --> zipVar
            )
          ),
          button(
            typ("button"), // default button type in HTML is "submit", we don't want it
            "Set SF",
            onClick.mapTo("94110") --> zipVar
          )
        ),

        p(button(typ("submit"), "Submit")),

        div(
          fontSize.percent(90),
          color("#777"),
          div("stateVar = ", text <-- stateVar.signal.map(_.toString)),
          div("cityVar = ", "\"", text <-- cityVar.signal, "\""),
          div("zipVar = ", "\"", text <-- zipVar.signal, "\""),
        )
      ),
      CodeSnippets(_.`form-state`) // Renders the code snippet that you can see online.
    )
  }

  private def renderInputRow(
    error: FormState => Option[String]
  )(
    mods: Modifier[HtmlElement]*
  ): HtmlElement = {
    val errorSignal = stateVar.signal.map(_.displayError(error))
    div(
      cls("-inputRow"),
      cls("x-hasError") <-- errorSignal.map(_.nonEmpty),
      mods,
      child.maybe <-- errorSignal.map(_.map(err => div(cls("-error"), err)))
    )
  }
}
// END[form-state]
