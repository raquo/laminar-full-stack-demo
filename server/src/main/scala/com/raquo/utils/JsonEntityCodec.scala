package com.raquo.utils

import cats.effect.IO
import io.bullet.borer.*
import org.http4s.*

/** These implicit instances allow http4s to decode JSON requests that our API receives,
  * and encode our API's responses in JSON.
  *
  * For these implicit instances to become available, they in turn need implicit instances
  * of [[https://sirthias.github.io/borer/ Borer]] Encoder-s and Decoder-s. You can see
  * how we derive those in e.g. [[com.raquo.weather.GradientReport]]
  */
object JsonEntityCodec {

  given jsonEntityEncoder[A](using Encoder[A]): EntityEncoder[IO, A] = {
    EntityEncoder.encodeBy[IO, A](
      headers.`Content-Type`(MediaType.application.json)
    ) { value =>
      val jsonByteArray = Json.encode(value).toByteArray
      val singleChunk = fs2.Chunk.array(jsonByteArray)
      val output: EntityBody[IO] = fs2.Stream.chunk(singleChunk)
      Entity(output)
    }
  }

  given jsonEntityDecoder[A](using Decoder[A]): EntityDecoder[IO, A] = {
    EntityDecoder.decodeBy[IO, A](MediaRange.`*/*`) { msg =>
      DecodeResult[IO, A](
        fs2.io.toInputStreamResource(msg.body).use { inputStream =>
          IO {
            Json.decode(inputStream).to[A].valueEither match {
              case Right(report) =>
                Right(report)
              case Left(err) =>
                // Can't get `err.cause` from Borer, it's private
                Left(MalformedMessageBodyFailure(err.getMessage, cause = None))
            }
          }
        }
      )
    }
  }
}
