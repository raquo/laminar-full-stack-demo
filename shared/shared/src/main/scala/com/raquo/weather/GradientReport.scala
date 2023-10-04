package com.raquo.weather

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

/**
  *
  * @param cities                  List of cities to display, in order
  * @param currentConditionsByCity cityId -> currentConditions.
  *                                Note: some records might be missing if current conditions are not available
  * @param forecastDays            list of day captions, in order to be displayed
  * @param forecastsByDate         date (day_caption -> cityId -> forecast)
  */
case class GradientReport(
  cities: List[CityStation],
  currentConditionsByCity: Map[String, CityCurrentConditions],
  forecastDays: List[String],
  forecastsByDay: Map[String, Map[String, CityForecast]]
)

object GradientReport {

  implicit val codec: JsonValueCodec[GradientReport] = JsonCodecMaker.make
}
