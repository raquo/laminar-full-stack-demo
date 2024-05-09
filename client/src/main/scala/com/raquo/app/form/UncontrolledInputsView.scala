package com.raquo.app.form

import com.raquo.app.JsRouter
import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.app.pages.ControlledInputsPage
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object UncontrolledInputsView {

  private val appStyles = List(
    marginTop.px(30),
    marginBottom.px(10)
  )

  def apply(): HtmlElement = {
    div(
      p(
        """"Uncontrolled input" basically means a unidirectional data binding: you're only listening to the user's input, your own code isn't updating the input element's value property with """,
        code("value <-- observable"),
        "."
      ),

      h2("1. Listening to user input"),
      renderListeningToUserInput(),
      CodeSnippets(_.`uncontrolled/listening`),

      h2("2. Transforming user input"),
      p("In other UI libraries if you want to ", b("transform"), " user input you need to use controlled components. We have that too, but for relatively simple cases you can use ", code("setAsValue"), " and ", code("setAsChecked"), " event processor operators instead."),
      p("The way it works is simple: when the event processor reaches the ", code("setAsValue"), " operator, it writes the string provided to it into ", code("event.target.value"), ". For this reason you shouldn't use the ", code("filter"), " event processor operator before ", code("setAsValue"), ": ", code("setAsValue"), " will not be called if the predicate doesn't match. Note that we use the ", code("map"), " operator here, not ", code("filter"), ". The filter we use is actually a method we call on String to transform it."),
      p("If you want to filter user input, for example if you want to prevent any input containing non-digits (as opposed to stripping out non-digits from such input), you should use Laminar ", a(JsRouter.navigateTo(ControlledInputsPage), "controlled inputs"), " instead."),
      renderTransformingUserInput(),
      CodeSnippets(_.`uncontrolled/transforming`),

      h2("3. Forms without vars"),
      p("You don't need to keep track of state in Vars. That is often useful, and more complex code tends to need that for auxiliary reasons, but you can fetch the state from the DOM instead:"),
      renderFormsWithoutVars(),
      CodeSnippets(_.`uncontrolled/form-no-vars`),
    )
  }

  private def renderListeningToUserInput(): HtmlElement = {
    // BEGIN[uncontrolled/listening]
    val inputTextVar = Var("")
    val checkedVar = Var(false)
    div(
      appStyles,
      p(
        label("Name: "),
        input(
          onInput.mapToValue --> inputTextVar
        )
      ),
      p(
        "You typed: ",
        text <-- inputTextVar
      ),
      p(
        label("I like to check boxes: "),
        input(
          typ("checkbox"),
          onInput.mapToChecked --> checkedVar
        )
      ),
      p(
        "You checked the box: ",
        text <-- checkedVar
      )
    )
    // END[uncontrolled/listening]
  }

  private def renderTransformingUserInput(): HtmlElement = {
    // BEGIN[uncontrolled/transforming]
    val zipVar = Var("")
    div(
      appStyles,
      p(
        label("Zip code: "),
        input(
          placeholder("12345"),
          maxLength(5), // HTML can help block some undesired input
          onInput
            .mapToValue
            .map(_.filter(Character.isDigit))
            .setAsValue --> zipVar
        )
      ),
      p(
        "Your zip code: ",
        text <-- zipVar
      ),
      button(
        onClick.mapTo(zipVar.now()) --> (zip => dom.window.alert(zip)),
        "Submit"
      )
    )
    // END[uncontrolled/transforming]
  }

  private def renderFormsWithoutVars(): HtmlElement = {
    // BEGIN[uncontrolled/form-no-vars]
    val inputEl = input(
      placeholder("12345"),
      maxLength(5), // HTML can help block some undesired input
      onInput
        .mapToValue
        .map(_.filter(Character.isDigit))
        .setAsValue --> Observer.empty
    )

    form(
      appStyles,
      onSubmit
        .preventDefault
        .mapTo(inputEl.ref.value) --> (zip => dom.window.alert(zip)),
      p(
        label("Zip code: "),
        inputEl
      ),
      p(
        button(typ("submit"), "Submit")
      )
    )
    // END[uncontrolled/form-no-vars]
  }


}
