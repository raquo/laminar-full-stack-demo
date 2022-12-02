package server

import cask.model.Request
import cask.model.Response.Raw
import cask.router.Result

final class StaticResourcesWithContentType(path: String) extends cask.staticResources(path) {
  override def wrapFunction(ctx: Request, delegate: Delegate): Result[Raw] = {

    val contentType =
      ctx.remainingPathSegments.last.reverse.takeWhile(_ != '.').reverse match {
        case "js"   => "application/javascript"
        case "css"  => "application/css"
        case "html" => "text/html; charset=utf-8"
        case other =>
          throw new UnsupportedOperationException(
            s"Don't know extension $other"
          )
      }
    super
      .wrapFunction(ctx, delegate)
      .map(result =>
        result.copy(headers =
          result.headers.filterNot(
            _._1.toLowerCase() == "content-type"
          ) :+ ("Content-Type" -> contentType)
        )
      )
  }
}
