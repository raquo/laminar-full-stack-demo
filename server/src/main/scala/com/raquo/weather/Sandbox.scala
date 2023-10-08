package com.raquo.weather

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

object Sandbox {

  case class Success[A] (result: A)

  object Success {
    given codec[A: Encoder: Decoder]: Codec[Success[A]] = deriveCodec
  }

  object XXX {
    case class Foo(foo: String)

    given fooCodec: Codec[Foo] = deriveCodec

    case class Bar[F <: Foo](foo: F, bar: Int)

    given barCodec[F <: Foo : Encoder : Decoder]: Codec[Bar[F]] = deriveCodec
  }

  object YYY {

    case class Foo(foo: String)

    case class Bar(bar: String)

    //given codec: Codec[Foo | Bar] = Codec.ForEither
  }

}
