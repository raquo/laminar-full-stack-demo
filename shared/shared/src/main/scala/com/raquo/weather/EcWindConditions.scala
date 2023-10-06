package com.raquo.weather

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class EcWindConditions(
  speedKmh: Double,
  direction: String, // e.g. "S", "VR"
  bearingDegrees: Double
)

object EcWindConditions {

  implicit val codec: JsonValueCodec[EcWindConditions] = JsonCodecMaker.make
}