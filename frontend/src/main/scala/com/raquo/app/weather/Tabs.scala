package com.raquo.app.weather

import com.raquo.laminar.api.L.*
import com.raquo.utils.JSImportSideEffect

import scala.scalajs.js

object Tabs {

  // Find and import the LESS (CSS) file for this component. See globResolverPlugin and importSideEffectPlugin
  JSImportSideEffect("@find/**/Tabs.less")

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
