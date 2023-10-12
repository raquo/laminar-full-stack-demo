package com.raquo.app.weather

import com.raquo.laminar.api.L.*

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Tabs {

  // #TODO Find a more compact way to reference a sibling file
  @js.native
  @JSImport("../../../../frontend/src/main/scala/com/raquo/app/weather/Tabs.less")
  private object Stylesheet extends js.Any

  private val _ = Stylesheet

  def apply(
    forecastDays: List[String],
    mods: (Option[String] => Mod[Button])*
  ): HtmlElement = {
    div(
      cls("Tabs"),
      (None :: forecastDays.map(Some(_))).map { maybeForecastDay =>
        button(
          cls("-tab"),
          maybeForecastDay.getOrElse("Now"),
          mods.map(_.apply(maybeForecastDay))
        )
      }
    )
  }
}
