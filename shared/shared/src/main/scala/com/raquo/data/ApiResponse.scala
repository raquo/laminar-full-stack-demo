package com.raquo.data

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*
import io.bullet.borer.derivation.key

sealed trait ApiResponse[+A]

object ApiResponse {

  @key("result")
  case class Result[+A](result: A) extends ApiResponse[A]

  @key("error")
  case class Error(message: String, statusCode: Int) extends Exception(message) with ApiResponse[Nothing]

  given successDecoder[A: Decoder]: Decoder[Result[A]] = Decoder[A].map(Result(_))

  given successEncoder[A: Encoder]: Encoder[Result[A]] = Encoder[A].contramap(_.result)

  given decoder[A: Decoder]: Decoder[ApiResponse[A]] = deriveAllDecoders

  given encoder[A: Encoder]: Encoder[ApiResponse[A]] = deriveAllEncoders
}
