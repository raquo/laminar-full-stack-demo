package entrypoint

import be.doeraene.webcomponents.ui5.configkeys.InputType
import be.doeraene.webcomponents.ui5.*
import business.SomeSharedData
import com.raquo.laminar.api.L.*
import org.scalajs.dom
import io.circe.syntax.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.scalajs.js

object EntryPoint {

  def main(args: Array[String]): Unit = {
    println("hello")

    def request(dataToSend: SomeSharedData) = new dom.RequestInit {
      method = dom.HttpMethod.POST
      body = dataToSend.asJson.noSpaces
      headers = js.Array(
        js.Array("Content-Type", "application/json"),
        js.Array("Accept", "text/plain")
      )
    }

    val clickBus: EventBus[Unit] = new EventBus
    val textToSendVar            = Var("")

    render(
      dom.document.getElementById("root"),
      div(
        Title.h1("Demo fly.io"),
        p(
          child.text <-- clickBus.events
            .sample(textToSendVar.signal)
            .flatMap(text =>
              EventStream.fromFuture(
                dom
                  .fetch("/api/do-thing", request(SomeSharedData(text, 2)))
                  .toFuture
                  .flatMap(_.text().toFuture),
                emitFutureIfCompleted = true
              )
            )
        ),
        div(
          display    := "flex",
          alignItems := "center",
          Label("Enter some text: "),
          Input(
            _.tpe := InputType.Text,
            _.events.onChange.mapToValue --> textToSendVar.writer,
            marginRight := "1em",
            marginLeft  := "1em"
          ),
          Button("Click me", _.events.onClick.mapTo(()) --> clickBus.writer)
        )
      )
    )
  }

}
