package com.raquo.weather

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

final case class SomeSharedData(text: String, number: Int)

object SomeSharedData {
  given codec: JsonValueCodec[SomeSharedData] = JsonCodecMaker.make
}
