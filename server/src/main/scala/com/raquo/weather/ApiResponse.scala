package com.raquo.weather

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*
import io.bullet.borer.derivation.key

sealed trait ApiResponse[+A] {
  val statusCode: Int
}

object ApiResponse {

  @key("result")
  case class Success[+A](result: A) extends ApiResponse[A] {
    override val statusCode: Int = 200
  }

  @key("error")
  case class Error(message: String, override val statusCode: Int) extends Exception(message) with ApiResponse[Nothing]

  given codec[A: Encoder: Decoder]: Codec[ApiResponse[A]] = deriveAllCodecs
}
