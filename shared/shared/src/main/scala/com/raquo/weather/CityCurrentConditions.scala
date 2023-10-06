package com.raquo.weather

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

import scala.scalajs.js.annotation.JSExportAll

/**
  * @param datetimeObserved        Time that current conditions were observed, in ISO 8601 format, local time
  * @param temperatureC            
  * @param pressureKPa             
  * @param relativeHumidityPercent Relative humidity in percent (0..100)
  * @param wind
  */
@JSExportAll
case class CityCurrentConditions(
  datetimeObserved: String,
  // iconCode: String,
  temperatureC: Option[Double],
  pressureKPa: Option[Double],
  relativeHumidityPercent: Option[Double],
  wind: Option[EcWindConditions]
)

object CityCurrentConditions {

  implicit val codec: JsonValueCodec[CityCurrentConditions] = JsonCodecMaker.make
}
