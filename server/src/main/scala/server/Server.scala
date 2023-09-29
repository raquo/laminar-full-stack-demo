package server

import io.circe.parser.decode
import business.SomeSharedData
import cask.model.Request
import cask.model.Response.Raw
import cask.router.Result

object Server extends cask.MainRoutes {

  override def port: Int = Option(System.getProperty("port")).fold(9000)(_.toInt)

  override def host: String = Option(System.getProperty("isProd"))
    .map(_.toBoolean)
    .fold("127.0.0.1")(isProd => if isProd then "0.0.0.0" else "127.0.0.1")

  @cask.get("/")
  def index() =
    cask.StaticResource(
      "static/index.html",
      getClass.getClassLoader,
      List("Content-Type" -> "text/html; charset=utf-8")
    )

  @cask.get("/api")
  def hello() = "Hello World!"

  @cask.get("/api/ping")
  def ping() = "PONG"

  @cask.post("/api/do-thing")
  def doThing(request: cask.Request) =
    decode[SomeSharedData](request.text()) match {
      case Left(err) => cask.Response(err.getMessage, statusCode = 400)
      case Right(value) =>
        cask.Response(s"I did something very cool with your $value")
    }

  @StaticResourcesWithContentType("/static")
  def staticResourceRoutes() = "static"

  initialize()

  println(s"Server listening at $host:$port")
}
