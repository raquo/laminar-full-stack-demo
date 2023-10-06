package com.raquo.server

import com.comcast.ip4s.{Host, Port}

import scala.concurrent.duration.*
import scala.util.Try

object ServerConfig {

  val isProd: Boolean = {
    Option(java.lang.System.getProperty("isProd")).map { isProdStr =>
      Try(isProdStr.toBoolean).getOrElse(throw new Exception(s"Bad isProd option: `${isProdStr}`. Expecting a scala boolean."))
    }.getOrElse(false)
  }

  private[server] val port: Port = {
    val portStr = Option(java.lang.System.getProperty("port")).getOrElse("9000")
    Port.fromString(portStr).getOrElse(throw new Exception(s"Bad port option: `${portStr}`"))
  }

  private[server] val host: Host = {
    val hostStr = if isProd then "0.0.0.0" else "127.0.0.1"
    Host.fromString(hostStr).getOrElse(throw new Exception(s"Bad host: `${hostStr}`"))
  }

  /** How long the http4s web server will keep the HTTP connection up after it was last used.
   *  - Low value in dev helps shut down the JVM fast when reloading the server.
   *  - On prod we want it higher in order to reduce the latency of subsequent requests.
   *    Another option is to define shutdownTimeout, which would force-kill the JVM.
   *    See [[https://discord.com/channels/632277896739946517/632286375311573032/1159309165076942898 Discord discussion]]
   */
  private[server] val idleTimeOut: Duration = if (isProd) 60.seconds else 2.seconds

}
