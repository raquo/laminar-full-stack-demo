package com.raquo.app.integrations

import be.doeraene.webcomponents.ui5.*
import be.doeraene.webcomponents.ui5.configkeys.*
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JsImportSideEffect
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object SapUI5WebComponentsView {

  JsImportSideEffect("@find/**/SapUI5WebComponentsView.less")

  def apply(): HtmlElement = {

    div(
      cls("SapUI5WebComponentsView"),
      Title(
        _.level := TitleLevel.H1,
        "SAP UI5 Web Components"
      ),

      Title(
        _.level := TitleLevel.H2,
        "Date picker"
      ),
      renderDatePicker(),

      Title(
        _.level := TitleLevel.H2,
        "Multi combo box"
      ),
      renderMultiComboBox(),

      Title(
        _.level := TitleLevel.H2,
        "Multi token input"
      ),
      renderMultiTokenInput()
    )
  }

  private def renderDatePicker(): HtmlElement = {
    val selectedDateVar: Var[String] = Var("2023-01-31")
    div(
      //Label(child.text <-- selectedTimeBus.events.map(value => s"Currently selected: $value")),
      //br(),
      DatePicker(
        value <-- selectedDateVar,
        _.events.onChange.mapToValue --> selectedDateVar
      )
    )
  }

  private def renderMultiComboBox(): HtmlElement = {
    val countries = List("Canada", "New Zealand", "Australia", "UK")
    MultiComboBox(
      _.placeholder := "Choose your countries",
      width := "300px",
      countries.zipWithIndex.map((country, index) =>
        MultiComboBox.item(_.text := country, _.selected := (index == 0))
      )
    )
  }

  private def renderMultiTokenInput(): HtmlElement = {
    val countries = List("Argentina", "Belgium", "Bulgaria", "Canada", "Columbia", "Croatia", "Denmark")

    val tokenValuesVar = Var(List("Argentina"))

    val changeBus: EventBus[String] = new EventBus

    // Emits the new values list, with whether or not they should be actually patched to the values
    // This check is required because we want tokens to be unique
    val newValuesWithShouldWeUpdate = changeBus.events
      .withCurrentValueOf(tokenValuesVar.signal)
      .map((newValue, previousValues) => (previousValues :+ newValue, !previousValues.contains(newValue)))

    // emits when token must be changed
    val newValuesChanges = newValuesWithShouldWeUpdate.collect { case (values, true) => values }

    // When the new value was already present, we issue the error message ...
    val valueStateBecomesErrorEvents = newValuesWithShouldWeUpdate.filter(!_._2).mapTo(ValueState.Error)
    // ... and we clear it 2 seconds later
    val valueStateBecomesNormalEvents = valueStateBecomesErrorEvents.delay(2000).mapTo(ValueState.None)

    val valueStateChanges = EventStream.merge(valueStateBecomesErrorEvents, valueStateBecomesNormalEvents)

    MultiInput(
      _.showSuggestions := true,
      _.valueState <-- valueStateChanges,
      width := "50%",
      _.slots.valueStateMessage := div("Token is already in the list"),
      countries.map(country => MultiInput.suggestion(_.text := country)),
      _.slots.tokens <-- tokenValuesVar.signal.map(_.map(tokenValue => MultiInput.token(_.text := tokenValue))),
      _.events.onChange.map(_.target.value) --> changeBus.writer,
      newValuesChanges --> tokenValuesVar.writer,
      value <-- changeBus.events.mapTo(""),
      _.events.onTokenDelete.map(_.detail.token.text) --> tokenValuesVar.updater((values, toRemove) =>
        values.filterNot(_ == toRemove)
      )
    )
  }
}
