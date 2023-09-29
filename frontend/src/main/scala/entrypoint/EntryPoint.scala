package entrypoint

import be.doeraene.webcomponents.ui5.configkeys.InputType
import be.doeraene.webcomponents.ui5.*
import business.SomeSharedData
import com.raquo.laminar.api.L.*
import org.scalajs.dom

import com.github.plokhotnyuk.jsoniter_scala.core.*

object EntryPoint {

  def main(args: Array[String]): Unit = {
    println("hello")

    val clickBus: EventBus[Unit] = new EventBus
    val textToSendVar = Var("")

    render(
      dom.document.getElementById("root"),
      div(
        Title.h1("Demo fly.io"),
        p(
          child.text <-- clickBus.events
            .sample(textToSendVar.signal)
            .flatMap(text =>
              FetchStream.post(
                url = "/api/do-thing",
                _.body(writeToString(SomeSharedData(text, 2))),
                _.headers(
                  "Content-Type" -> "application/json",
                  "Accept" -> "text/plain"
                )
              )
            )
        ),
        div(
          display := "flex",
          alignItems := "center",
          Label("Enter some text: "),
          Input(
            _.tpe := InputType.Text,
            _.events.onChange.mapToValue --> textToSendVar.writer,
            marginRight := "1em",
            marginLeft := "1em"
          ),
          Button("Click me", _.events.onClick.mapTo(()) --> clickBus.writer)
        )
      )
    )
  }

}
