package com.raquo.server

import cats.effect.*
import com.raquo.server.Utils.*
import com.raquo.weather.{ApiError, WeatherFetcher}
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.staticcontent.resourceServiceBuilder

object Server extends IOApp {

  // --

  private val staticAssetsService = resourceServiceBuilder[IO]("/static").toRoutes

  private val httpService = HttpRoutes.of[IO] {

    // You must serve the index.html file that loads your frontend code for
    // every url that is defined in your frontend (Waypoint) routes, in order
    // for users to be able to navigate to these URLs from outside of your app.
    case request @ GET -> Root =>
      StaticFile.fromResource("/static/index.html", Some(request)).getOrElseF(InternalServerError())

    // This route covers all URLs under `/app`, including `/app` and `/app/`.
    case request @ GET -> "app" /: _ =>
      StaticFile.fromResource("/static/index.html", Some(request)).getOrElseF(InternalServerError())

    // Vite moves index.html into the public directory, but we don't want
    // users to navigate manually to /index.html in the browser, because
    // that route is not defined in Waypoint, we use `/` instead.
    case GET -> Root / "index.html" =>
      TemporaryRedirect(headers.Location(uri"/"))

    case GET -> Root / "ping" =>
      Ok(s"Pong")

    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")

  }

  private val httpApiService = HttpRoutes.of[IO] {

    case GET -> Root / "gradient" / gradientId =>
      WeatherFetcher
        .fetchGradient(gradientId)
        .attempt.flatMap {
          case Right(report) =>
            Ok(report.toString) // #TODO json encoding
          case Left(err: ApiError) =>
            CustomStatusCode(err.httpStatusCode)(err.message)
          case Left(otherErr) =>
            InternalServerError(otherErr.toString)
        }
  }

  private val app = Router.define(
    "/" -> httpService,
    "/api" -> httpApiService
  )(default = staticAssetsService).orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withIdleTimeout(ServerConfig.idleTimeOut)
      .withHost(ServerConfig.host)
      .withPort(ServerConfig.port)
      .withHttpApp(app)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)

}
