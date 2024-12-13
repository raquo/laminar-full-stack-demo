package com.raquo.app.basic

import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

import scala.scalajs.js.RegExp

object TimeView {

  private val appStyles = List(
    marginTop.px(10), // CSS unit helpers. These work for <-- too, e.g. `paddingTop.px <-- observableOfInt`
    marginBottom.px(10),
  )

  def apply(): HtmlElement = {
    div(
      h2("1. Basic interval stream"),
      renderBasicIntervalStream(),
      CodeSnippets(_.`time/basic`),
      //
      h2("2. Delay"),
      p("Observables naturally support asynchrony. In this example, on every click, we render a \"Just clicked\" message, and also schedule its removal 500ms later. When such a component is unmounted, its streams are stopped automatically, no need for manual cleanup or isMounted checks."),
      renderDelay(),
      CodeSnippets(_.`time/delay`),
      //
      h2("3. Debounce"),
      p("This example uses typical debounce logic to delay displaying of \"invalid email\" error until you stop typing."),
      renderDebounce(),
      CodeSnippets(_.`time/debounce`)
    )
  }

  private def renderBasicIntervalStream(): HtmlElement = {
    // BEGIN[time/basic]
    val tickStream = EventStream.periodic(1000)

    div(
      appStyles,
      div(
        "Tick #: ",
        text <-- tickStream.map(_.toString)
      ),
      div(
        "Random #: ",
        text <-- tickStream.mapTo(scala.util.Random.nextInt() % 100)
      )
    )
    // END[time/basic]
  }

  private def renderDelay(): HtmlElement = {
    // BEGIN[time/delay]
    val clickBus = new EventBus[Unit]

    val maybeAlertStream = EventStream.merge(
      clickBus.events.mapTo(Some(span("Just clicked!"))),
      clickBus.events.delay(500).mapTo(None)
    )

    div(
      appStyles,
      button(onClick.mapTo(()) --> clickBus, "Click me"),
      child.maybe <-- maybeAlertStream
    )
    // END[time/delay]
  }

  private def renderDebounce(): HtmlElement = {
    // BEGIN[time/debounce]
    val emailRegex = new RegExp("^[^@]+@[^@]+\\.[^@]+$")

    def emailError(email: String): Option[String] =
      if (email.isEmpty)
        Some("Please fill out email")
      else if (!emailRegex.test(email))
        Some("Invalid email!")
      else
        None

    val inputBus = new EventBus[String]

    val debouncedErrorStream: EventStream[Option[String]] =
      inputBus.events
        .debounce(1000)
        .map(emailError)

    div(
      appStyles,
      span(
        label("Your email: "),
        input(
          value <-- inputBus.events,
          onInput.mapToValue --> inputBus
        )
      ),
      child <-- debouncedErrorStream.map {
        case Some(err) => span(cls("u-error"), "Error: " + err)
        case None => span(cls("u-success"), "Email ok!")
      }
    )
    // END[time/debounce]
  }

}
