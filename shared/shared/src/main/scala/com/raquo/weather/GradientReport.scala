package com.raquo.weather

import scala.scalajs.js.annotation.JSExportAll

// BEGIN[wind-gradient]
import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

/**
  *
  * @param cities                  List of cities to display, in order
  * @param currentConditionsByCity cityId -> currentConditions.
  *                                Note: some records might be missing if current conditions are not available
  * @param forecastDays            list of day captions, in order to be displayed
  * @param forecastsByDay          (day_caption -> cityId -> forecast)
  */
@JSExportAll
case class GradientReport(
  cities: List[CityStation],
  currentConditionsByCity: Map[String, CityCurrentConditions],
  forecastDays: List[String],
  forecastsByDay: Map[String, Map[String, CityForecast]]
) {

  def selectCurrentConditions[A](
    f: CityCurrentConditions => Option[A]
  ): List[Option[A]] = {
    cities.flatMap { city =>
      for {
        conditions <- currentConditionsByCity.get(city.id)
      } yield {
        f(conditions)
      }
    }
  }

  def selectForecasts[A](
    day: String,
    f: CityForecast => Option[A]
  ): List[Option[A]] = {
    cities.flatMap { city =>
      for {
        forecastsByCity <- forecastsByDay.get(day)
        forecast <- forecastsByCity.get(city.id)
      } yield {
        f(forecast)
      }
    }
  }
}

object GradientReport {

  given codec: Codec[GradientReport] = deriveCodec
}
// END[wind-gradient]
