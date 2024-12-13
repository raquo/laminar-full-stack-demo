package com.raquo.app.weather

import com.raquo.laminar.api.L.*
import com.raquo.utils.Utils.useImport

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Tabs {

  // Find and import the LESS (CSS) file for this component.
  // See https://github.com/raquo/vite-plugin-glob-resolver
  // See https://github.com/raquo/vite-plugin-import-side-effect
  @js.native @JSImport("@find/**/Tabs.less", JSImport.Namespace)
  private object Stylesheet extends js.Object

  useImport(Stylesheet)

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
