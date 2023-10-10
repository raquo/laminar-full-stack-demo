package com.raquo.app.weather

import com.raquo.airstream.core.Observable
import com.raquo.app.JsRouter.*
import com.raquo.app.jsonApiDecoder
import com.raquo.app.pages.WeatherGradientPage
import com.raquo.data.ApiResponse
import com.raquo.laminar.api.L.*
import com.raquo.utils.Url.*
import com.raquo.weather.{Gradient, GradientReport}

object WeatherGradientView {

  def apply(pageS: Signal[WeatherGradientPage]): HtmlElement = {

    // #Note: def is important here, but hopefully not for long, looking to fix it in 17.0.0
    def gradientS = pageS.flatMap { p =>
      Gradient.forId(p.gradientId) match {
        case Some(gradient) =>
          EventStream.fromValue(gradient)
        case None =>
          forcePage(NotFoundPage)
          EventStream.empty
      }
    }

    val apiResponseS = gradientS.flatMap { gradient =>
      println(gradient)
      FetchStream
        .withDecoder(jsonApiDecoder[GradientReport])
        .get(absRoot / "api" / "weather" / "gradient" / gradient.id)
    }
    val gradientReportS = apiResponseS.collect {
      case ApiResponse.Result(report) => report
    }
    val maybeApiErrorS = apiResponseS.collect {
      case ApiResponse.Error(msg, _) => Some(msg)
      case _ => None
    }

    div(
      h1(child.text <-- gradientS.map(_.name + " gradient")),
      //child.text <-- gradientS.map { gradient => gradient.id }.setDisplayName("gradientS.flatmap")
      child.maybe <-- maybeApiErrorS.map(_.map(err => div(textAlign.center, span(cls("u-error"), err)))),
      child <-- gradientReportS.map { r => div(r.toString) },
      //child.text <--
      //    .map {
      //      case ApiResponse.Result(report) =>
      //      case ApiResponse.Error(message, _)
      //    }
      //}
    )
  }
}
