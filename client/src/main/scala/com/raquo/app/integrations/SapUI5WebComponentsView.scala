package com.raquo.app.integrations

import be.doeraene.webcomponents.ui5.*
import be.doeraene.webcomponents.ui5.configkeys.*
import com.raquo.app.JsRouter.{navigateTo, titleLink}
import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.app.pages.ShoelaceWebComponentsPage
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.Utils.useImport
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object SapUI5WebComponentsView {

  @js.native @JSImport("@find/**/SapUI5WebComponentsView.less", JSImport.Namespace)
  private object Stylesheet extends js.Object

  useImport(Stylesheet)

  def apply(): HtmlElement = {
    div(
      cls("SapUI5WebComponentsView"),
      //
      p("All of the below are SAP UI5 components – titles, date picker, combo boxes, buttons. Some of them are custom-styled using different methods."),
      p(a("SAP UI5", href("https://sap.github.io/ui5-webcomponents/playground/")), " is a pretty decent library of web components. We use it via ", a("LaminarSAPUI5Bindings", href("https://github.com/sherpal/LaminarSAPUI5Bindings")), " – good, manually crafted UI5 bindings designed specifically for Laminar. ", a("See live demo", href("https://sherpal.github.io/laminar-ui5-demo/"))),
      p("SAP UI5 works well, but is not very customizable visually. They have some themes that let you change the general appearance, but they don't seem to use CSS parts for most of their components, so injecting custom styles into their components requires some hacks (not shown here)."),
      p("See also: ", a(navigateTo(ShoelaceWebComponentsPage), "Shoelace Web Components"), "."),

      // Most concise syntax using Scala 3 union types.
      // Not supported in Scala 2. Not supported by IntelliJ yet.
      // https://github.com/sherpal/LaminarSAPUI5Bindings#remark-for-scala-213-users
      // https://youtrack.jetbrains.com/issue/SCL-21713/Method-accepting-a-union-of-types-that-includes-a-Function-type-problems-with-go-to-definition-type-hints-and-autocomplete-Scala
      // BEGIN[ui5-title]
      Title(
        _.level := TitleLevel.H1,
        "SAP UI5 Web Components"
      ),
      // END[ui5-title]
      CodeSnippets(_.`ui5-title`, caption = "Sources for UI5 Title component above:"),

      // Alternative syntax, supported by Scala 2, and IntelliJ.
      // Requires calling the .of method, and prepending regular
      // modifiers with "_ => " due to lack of union types.
      Title.of(
        _.level := TitleLevel.H2,
        _ => "Date picker",
        _ => titleLink("date-picker")
      ),
      renderDatePicker(),
      CodeSnippets(_.`ui5/renderDatePicker`),
      //
      Title.of(
        _.level := TitleLevel.H2,
        _ => "Multi combo box",
        _ => titleLink("multi-combo-box")
      ),
      renderMultiComboBox(),
      CodeSnippets(_.`ui5/renderMultiComboBox`),
      //
      Title.of(
        _.level := TitleLevel.H2,
        _ => "Buttons",
        _ => titleLink("buttons")
      ),
      div(renderButtons()),
      CodeSnippets(_.`ui5/renderButtons`)
    )
  }

  private def renderButtons(): List[Modifier.Base] = {
    // BEGIN[ui5/renderButtons]
    List(
      Button.of(
        _ => "CLICK ME",
        _ => onClick --> { _ => dom.window.alert("YOUR COMPLIANCE IS APPRECIATED") }
      ),
      " ",
      Button.of(
        _ => cls("x-yellow"),
        _ => "Button styled via CSS", // See SapUI5WebComponentsView.less
        _ => onClick --> { _ => dom.window.alert("Ok") }
      ),
      " ",
      Button.of(
        _ => backgroundColor := "green",
        _ => borderColor := "green",
        _ => color.white,
        _ => fontWeight.bold,
        _ => "Button styled via Laminar",
        _ => onClick --> { _ => dom.window.alert("Ok") }
      )
    )
    // END[ui5/renderButtons]
  }

  // A few examples borrowed from the https://github.com/sherpal/LaminarSAPUI5Bindings demo page.

  private def renderDatePicker(): HtmlElement = {
    // BEGIN[ui5/renderDatePicker]
    val selectedDateVar: Var[String] = Var("2023-01-31")
    div(
      Label(
        "Currently selected: ",
        text <-- selectedDateVar.signal
      ),
      br(),
      DatePicker.of(
        _ => value <-- selectedDateVar,
        _.events.onChange.mapToValue --> selectedDateVar
      )
    )
    // END[ui5/renderDatePicker]
  }

  private def renderMultiComboBox(): HtmlElement = {
    // BEGIN[ui5/renderMultiComboBox]
    val countries = List("Argentina", "Australia", "Belgium", "Bulgaria", "Canada", "Columbia", "Croatia", "Denmark", "Canada", "New Zealand", "UK", "USA")
    val selectedCountriesVar = Var(List(countries.head))
    div(
      div(
        Label(
          "Currently selected: ",
          text <-- selectedCountriesVar.signal.map(_.mkString(", "))
        )
      ),
      MultiComboBox.of(
        _.placeholder := "Choose your countries",
        _ => width.px := 300,
        _.events.onSelectionChange.map(_.detail.items.map(_.text).toList) --> selectedCountriesVar,
        _ =>
          countries.map { country =>
            MultiComboBox.item(
              _.text := country,
              _.selected <-- selectedCountriesVar.signal.map(_.contains(country))
            )
          }
      ),
      " ",
      Button(
        onClick.mapTo(Nil) --> selectedCountriesVar,
        "Clear"
      )
    )
    // END[ui5/renderMultiComboBox]
  }
}
