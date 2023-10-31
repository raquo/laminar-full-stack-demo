package com.raquo.app.form

import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect
import org.scalajs.dom

// BEGIN[form-state]
object FormStateView {

  // Find and import the LESS (CSS) file for this component. See globResolverPlugin and importSideEffectPlugin
  JSImportSideEffect("@find/**/FormStateView.less")

  private val stateVar = Var(FormState())

  private val zipWriter = stateVar.updater[String]((state, zip) => state.copy(zip = zip))

  private val descriptionWriter = stateVar.updater[String]((state, desc) => state.copy(city = desc))

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
              value <-- stateVar.signal.map(_.city),
              onInput.mapToValue --> descriptionWriter
            )
          ),
          button(
            typ("button"), // "submit" is the default in HTML
            "Clear",
            onClick.mapTo("") --> descriptionWriter
          )
        ),

        renderInputRow(_.zipError)(
          label("Zip code: "),
          input(
            cls("-zipCodeInput"),
            placeholder("12345"),
            controlled(
              value <-- stateVar.signal.map(_.zip),
              onInput.mapToValue.filter(_.forall(Character.isDigit)) --> zipWriter
            )
          ),
          button(
            typ("button"), // default button type in HTML is "submit", we don't want it
            "Set SF",
            onClick.mapTo("94110") --> zipWriter
          )
        ),
        
        button(typ("submit"), "Submit")
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
      cls.toggle("x-hasError") <-- errorSignal.map(_.nonEmpty),
      mods,
      child.maybe <-- errorSignal.map(_.map(err => div(cls("-error"), err)))
    )
  }
}
// END[form-state]
