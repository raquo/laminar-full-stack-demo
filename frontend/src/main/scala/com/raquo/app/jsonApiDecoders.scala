package com.raquo.app

import com.raquo.data.ApiResponse
import com.raquo.laminar.api.L.*
import io.bullet.borer.*
import org.scalajs.dom

import scala.scalajs.js
import scala.util.{Failure, Success, Try}

def decodeJsArrayBuffer[A: Decoder](arrayBuffer: js.typedarray.ArrayBuffer): Try[A] = {
  // See https://github.com/sirthias/borer/issues/675
  val jsByteArray = new js.typedarray.Uint8Array(arrayBuffer)
  val numBytes = arrayBuffer.byteLength
  val byteArray = new Array[Byte](numBytes)
  var i = 0
  while (i < numBytes) {
    // Note: .asInstanceOf is safe because both Short and Byte
    // have the same runtime representation in JS (Number)
    byteArray(i) = jsByteArray(i).asInstanceOf[Byte]
    i += 1
  }
  js.typedarray.TypedArrayBuffer.wrap(arrayBuffer)
  Json.decode(byteArray).to[A].valueTry
}

def jsonApiDecoder[A: Decoder](response: dom.Response): EventStream[ApiResponse[A]] = {
  EventStream
    .fromJsPromise(response.arrayBuffer())
    .map { arrayBuffer =>
      println("....")
      decodeJsArrayBuffer[ApiResponse[A]](arrayBuffer) match {
        case Success(response) => response
        // #TODO this will "fake" a 500 http status code, which is not there. Bad.
        //   - Definitely fix the structure. Maybe make the code an option?
        case Failure(decodingErr) => ApiResponse.Error("Failed decoding API JSON: " + decodingErr.getMessage, statusCode = 500)
      }
    }
    .setDisplayName("jsonApiDecoder stream")
    .debugSpy(_ => println("json decoded"))
}
