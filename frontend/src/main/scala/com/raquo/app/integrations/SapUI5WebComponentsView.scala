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
      // Most concise syntax using Scala 3 union types.
      // Not supported in Scala 2. Not supported by IntelliJ yet.
      // https://github.com/sherpal/LaminarSAPUI5Bindings#remark-for-scala-213-users
      // https://youtrack.jetbrains.com/issue/SCL-21713/Method-accepting-a-union-of-types-that-includes-a-Function-type-problems-with-go-to-definition-type-hints-and-autocomplete-Scala
      Title(
        _.level := TitleLevel.H1,
        "SAP UI5 Web Components"
      ),

      // Alternative syntax, supported by Scala 2, and IntelliJ.
      // Requires calling the .of method, and prepending regular
      // modifiers with "_ => " due to lack of union types.
      Title.of(
        _.level := TitleLevel.H2,
        _ => "Date picker"
      ),
      renderDatePicker(),

      Title.of(
        _.level := TitleLevel.H2,
        _ => "Multi combo box"
      ),
      renderMultiComboBox(),

      Title.of(
        _.level := TitleLevel.H2,
        _ => "Multi token input"
      ),
      renderMultiTokenInput()
    )
  }

  // A few examples borrowed from the https://github.com/sherpal/LaminarSAPUI5Bindings demo page.

  private def renderDatePicker(): HtmlElement = {
    val selectedDateVar: Var[String] = Var("2023-01-31")
    div(
      //Label(child.text <-- selectedTimeBus.events.map(value => s"Currently selected: $value")),
      //br(),
      DatePicker.of(
        _ => value <-- selectedDateVar,
        _.events.onChange.mapToValue --> selectedDateVar
      )
    )
  }

  private def renderMultiComboBox(): HtmlElement = {
    val countries = List("Canada", "New Zealand", "Australia", "UK")
    MultiComboBox.of(
      _.placeholder := "Choose your countries",
      _ => width := "300px",
      _ => countries.zipWithIndex.map((country, index) =>
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

    MultiInput.of(
      _.showSuggestions := true,
      _.valueState <-- valueStateChanges,
      _ => width := "50%",
      _.slots.valueStateMessage := div("Token is already in the list"),
      _ => countries.map(country => MultiInput.suggestion(_.text := country)),
      _.slots.tokens <-- tokenValuesVar.signal.map(_.map(tokenValue => MultiInput.token(_.text := tokenValue))),
      _.events.onChange.map(_.target.value) --> changeBus.writer,
      _ => newValuesChanges --> tokenValuesVar.writer,
      _ => value <-- changeBus.events.mapTo(""), // clear input text when selecting a value
      _.events.onTokenDelete.map(_.detail.token.text) --> tokenValuesVar.updater((values, toRemove) =>
        values.filterNot(_ == toRemove)
      )
    )
  }
}
