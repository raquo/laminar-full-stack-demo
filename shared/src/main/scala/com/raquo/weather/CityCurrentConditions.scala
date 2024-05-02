package com.raquo.weather

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

import scala.scalajs.js.annotation.JSExportAll

/**
  * @param localDatetimeObserved        Time that current conditions were observed, in ISO 8601 format, local time
  * @param temperatureC
  * @param pressureKPa
  * @param relativeHumidityPercent Relative humidity in percent (0..100)
  * @param wind
  */
@JSExportAll
case class CityCurrentConditions(
  localDatetimeObserved: String,
  // iconCode: String,
  temperatureC: Option[Double],
  pressureKPa: Option[Double],
  relativeHumidityPercent: Option[Double],
  wind: Option[EcWindConditions]
)

object CityCurrentConditions {

  given codec: Codec[CityCurrentConditions] = deriveCodec
}
