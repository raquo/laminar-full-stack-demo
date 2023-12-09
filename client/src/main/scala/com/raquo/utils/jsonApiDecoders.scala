package com.raquo.app

import com.raquo.data.ApiResponse
import com.raquo.laminar.api.L.*
import io.bullet.borer.*
import org.scalajs.dom

import java.nio.ByteOrder
import scala.scalajs.js
import scala.util.{Failure, Success, Try}

def decodeJsArrayBuffer[A: Decoder](arrayBuffer: js.typedarray.ArrayBuffer): Try[A] = {
  // There are several possible implementations of this conversion. I haven't tested, but
  // I think this one is probably the most efficient.
  // See https://github.com/sirthias/borer/issues/675
  val buffer = js.typedarray.TypedArrayBuffer.wrap(arrayBuffer)
  buffer.order(ByteOrder.BIG_ENDIAN) // Borer requires big endian order to parse ByteBuffer-s
  Json.decode(buffer).to[A].valueTry
}

def jsonApiDecoder[A: Decoder](response: dom.Response): EventStream[ApiResponse[A]] = {
  EventStream
    .fromJsPromise(response.arrayBuffer())
    .map { arrayBuffer =>
      decodeJsArrayBuffer[ApiResponse[A]](arrayBuffer) match {
        case Success(response) => response
        // #TODO this will "fake" a 500 http status code, which is not there. Bad.
        //   - Definitely fix the structure. Maybe make the code an option?
        case Failure(decodingErr) => ApiResponse.Error("Failed decoding API JSON: " + decodingErr.getMessage, statusCode = 500)
      }
    }
}
