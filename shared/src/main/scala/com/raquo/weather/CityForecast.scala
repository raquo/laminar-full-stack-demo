package com.raquo.weather

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

import scala.scalajs.js.annotation.JSExportAll

/**
 * @param dateTimeIssued  Time that the forecast was issued at.
 * @param iconCode        Two-digit weather conditions code. See https://eccc-msc.github.io/open-data/msc-data/citypage-weather/readme_citypageweather-datamart_en/#icons-of-the-xml-product
 * @param temperatureC    Temperature in degrees Celsius
 */
@JSExportAll
case class CityForecast(
  dateTimeIssued: String,
  iconCode: String,
  temperatureC: Double
)

object CityForecast {

  given codec: Codec[CityForecast] = deriveCodec
}
