package com.raquo.app.weather

import app.tulz.tuplez.Composition
import com.raquo.app.pages.WeatherGradientPage
import com.raquo.laminar.api.L.*
import com.raquo.weather.Gradient
import com.raquo.waypoint.*
import urldsl.language.{PathSegment, UrlPart}
import urldsl.vocabulary.UrlMatching
import com.raquo.utils.Url.*

import scala.scalajs.js

object WeatherGradientView {

  def apply(pageS: Signal[WeatherGradientPage]): HtmlElement = {
    val gradientS = pageS.map(p => Gradient.forId(p.gradientId))
    div(
      h1(child.text <-- gradientS.map(_.name + " gradient")),
      child.text <-- gradientS.flatMap { gradient =>
        FetchStream.get(
          absRoot / "api" / "weather" / "gradient" / gradient.id
        )
      }
    )
  }
}
