package server

// import io.circe.parser.decode
import business.SomeSharedData
import com.github.plokhotnyuk.jsoniter_scala.core.*

import scala.util.{Failure, Success, Try}
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.scala.implicits.*
import com.linecorp.armeria.server.file.FileService
import com.linecorp.armeria.server.{Server, ServerBuilder}

import java.net.InetSocketAddress
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
import scala.concurrent.{ExecutionContext, Future}

object ArmeriaServer {

  private def port: Int = Option(java.lang.System.getProperty("port")).fold(9000)(_.toInt)

  private def host: String = Option(java.lang.System.getProperty("isProd"))
    .map(_.toBoolean)
    .fold("127.0.0.1")(isProd => if isProd then "0.0.0.0" else "127.0.0.1")

  // --

  private val sb: ServerBuilder = Server.builder()

  sb.http(InetSocketAddress(host, port))

  sb.service("/test/{name}", (ctx, req) => {
    HttpResponse.of(
      s"""
         |localAddress = ${ctx.localAddress()}
         |uri = ${ctx.uri()}
         |path = ${ctx.path()}
         |ctx.pathParam("name") = ${ctx.pathParam("name")}
         |ctx.queryParams = ${ctx.queryParams()}
         |${ctx.queryParams()}
         |""".stripMargin
    )
  })

  // index.html file is located at root, but we don't want people to have "index.html"
  // in the URL if they somehow manually navigate to it, so we redirect to root URL.
  sb.service("/index.html", (ctx, req) => HttpResponse.ofRedirect("/"))

  println()

  private val staticFileService = FileService
    .builder(ClassLoader.getSystemClassLoader(), "static")
    // .maxCacheEntries(0)
    .build()

  sb.serviceUnder("/", staticFileService)

  val server: Server = sb.build()

  def main(args: Array[String]): Unit = {
    println(">>> STARTING Armeria server...")
    val future = server.start()
    Try(future.join()) match {
      case Success(_) => println(s"<<< Armeria server started, listening at ${server.activePort().localAddress()}")
      case Failure(_) => println(">> FAILED TO START Armeria server")
    }
  }
}



//   @cask.post("/api/do-thing")
//   def doThing(request: cask.Request) =
//     Try(readFromString[SomeSharedData](request.text())) match {
//       case Failure(err: JsonReaderException) => cask.Response(err.getMessage, statusCode = 400)
//       case Failure(err) => cask.Response(err.getMessage, statusCode = 500)
//       case Success(value) => cask.Response(s"I did something cool with your $value")
//     }
//

