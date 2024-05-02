package com.raquo.weather

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
case class EcWindConditions(
  speedKmh: Double,
  direction: String, // e.g. "S", "VR"
  bearingDegrees: Double
)

object EcWindConditions {

  given codec: Codec[EcWindConditions] = deriveCodec
}
