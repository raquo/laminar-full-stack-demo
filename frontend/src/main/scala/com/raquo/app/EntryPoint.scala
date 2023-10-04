package com.raquo.app

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.raquo.weather.SomeSharedData
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

object EntryPoint {

  def main(args: Array[String]): Unit = {
    println("hello")

    val clickBus: EventBus[Unit] = new EventBus
    val textToSendVar = Var("")

    render(
      dom.document.getElementById("root"),
      div(
        h1("Weather demo+"),
        div(
          child.text <-- FetchStream.post(
            url = "/api/do-thing",
            _.body(writeToString(SomeSharedData("hello", 2))),
            _.headers(
              "Content-Type" -> "application/json",
              "Accept" -> "text/plain"
            )
          )
        )
      )
    )
  }

}
