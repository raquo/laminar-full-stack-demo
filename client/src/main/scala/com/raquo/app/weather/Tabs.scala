package com.raquo.app.weather

import com.raquo.laminar.api.L.*
import com.raquo.utils.JSImportSideEffect

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
          span(cls("u-hideOnMobile"), maybeForecastDay.getOrElse("Now")),
          span(cls("u-hideOnDesktop"), maybeForecastDay.map(_.substring(0, 3)).getOrElse("Now")),
          mods.map(_.apply(maybeForecastDay))
        )
      }
    )
  }
}
