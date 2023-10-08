package com.raquo.app

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

object pages {

  sealed abstract class Page(val title: String)

  case object HomePage extends Page("Home")

  case class WeatherGradientPage(gradientId: String) extends Page(s"Weather Gradient") // #TODO update title to match content

  given pageCodec: Codec[Page] = deriveAllCodecs

}
