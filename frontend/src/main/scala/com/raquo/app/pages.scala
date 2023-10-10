package com.raquo.app

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

object pages {

  sealed abstract class Page(val title: String)

  case object HomePage extends Page("Home")

  case object NotFoundPage extends Page("Not Found :(")

  case class WeatherGradientPage(gradientId: String) extends Page(s"Weather Gradient") // #TODO update title to match content

  // This page does not have a route defined for it, to show you what happens in those cases.
  case class UnroutedPage(foo: String) extends Page(s"Unrouted page")

  given pageCodec: Codec[Page] = deriveAllCodecs

}
