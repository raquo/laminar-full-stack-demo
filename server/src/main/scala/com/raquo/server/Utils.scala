package com.raquo.server

//import cats.effect.IO
import cats.effect.*
import cats.syntax.all.*
//import org.http4s.dsl.io.*
//import org.http4s.implicits.*
import org.http4s.*
//import org.http4s.headers.`Content-Length`

object Utils {

  extension [L, R] (either: Either[L, R])

    def getRight: R = {
      either match {
        case Right(v) => v
        case Left(v) => throw new Exception(s"Called .getRight on Left($v)")
      }
    }

    def getLeft: L = {
      either match {
        case Left(v) => v
        case Right(v) => throw new Exception(s"Called .getLeft on Right($v)")
      }
    }

  extension [A] (io: IO[A])

    def tap(sideEffect: A => Unit): IO[A] = {
      io.flatTap(v => IO { sideEffect(v) })
    }

    def tapError(sideEffect: Throwable => Unit): IO[A] = {
      io.onError(err => IO {
        sideEffect(err)
      })
    }

  //extension (status: Status)
  //
  //  def raw(body: String, headers: Header.ToRaw*)(
  //    implicit w: EntityEncoder[IO, String],
  //  ): Response[IO] = {
  //    val entity = w.toEntity(body)
  //    val contentSizeHeader = entity.length.flatMap(x => `Content-Length`.fromLong(x).toOption)
  //    val allHeaders = w.headers |+| Headers(contentSizeHeader, headers)
  //
  //    Response[IO](status, headers = allHeaders, body = entity.body)
  //  }

  def CustomStatusCode[A](statusCode: Int)(body: A)(using EntityEncoder[IO, A]): IO[Response[IO]] = {
    Response(Status.fromInt(statusCode).getRight).withEntity(body).pure[IO]
  }

}
