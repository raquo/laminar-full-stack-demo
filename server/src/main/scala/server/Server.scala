package server

import business.SomeSharedData

import com.github.plokhotnyuk.jsoniter_scala.core.*
import io.javalin.Javalin
import io.javalin.http.HttpStatus
import io.javalin.http.staticfiles.{Location, StaticFileConfig}

import java.nio.charset.StandardCharsets
import java.util.function.Consumer
import scala.util.{Failure, Success, Try}
// import server.Utils.given

object Server {

  private def port = Option(java.lang.System.getProperty("port")).fold(9000)(_.toInt)

  private def host: String = Option(java.lang.System.getProperty("isProd"))
    .map(_.toBoolean)
    .fold("127.0.0.1")(isProd => if isProd then "0.0.0.0" else "127.0.0.1")

  // --

  def main(args: Array[String]): Unit = {
    val app = Javalin.create(config => {
      config.staticFiles.add((staticFiles: StaticFileConfig) => {
        staticFiles.hostedPath = "/"
        staticFiles.directory = "static"
        staticFiles.location = Location.CLASSPATH
        staticFiles.skipFileFunction = ctx => {
          val skip = ctx.getPathInfo == "/index.html"
          println(">>> " + ctx.getPathInfo + "  skip? " + skip)
          skip
        }
      })
      // config.spaRoot.addFile("/", "static/index.html", Location.CLASSPATH)
    })

    app.get("/", ctx => {
      ctx.html(Utils.getResourceFileAsString("static/index.html", StandardCharsets.UTF_8))
    })

    app.get("/ping", ctx => {
      ctx.result("pong")
    })

    app.post("/api/do-thing", ctx => {
      Try(readFromString[SomeSharedData](ctx.body())) match {
        case Failure(err: JsonReaderException) =>
          ctx.status(HttpStatus.BAD_REQUEST)
          ctx.result(err.getMessage)
        case Failure(err) =>
          ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
          ctx.result(err.getMessage)
        case Success(value) =>
          ctx.result(s"I did something cool with your $value")
      }
    })

    app.start(host, port)
  }

}
