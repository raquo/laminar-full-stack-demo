package business

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

final case class SomeSharedData(text: String, number: Int)

object SomeSharedData {
  given Codec[SomeSharedData] = deriveCodec
}
