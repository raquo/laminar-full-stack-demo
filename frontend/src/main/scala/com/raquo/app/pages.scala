package com.raquo.app

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

object pages {

  sealed trait Page(val title: String)

  // In our code extending this trait means we auto-render an
  // h1 element for this page, just for kicks
  sealed abstract class TitledPage(title: String) extends Page(title)

  case object HomePage extends TitledPage("Laminar Demo")

  case object HelloWorldPage extends TitledPage("Hello world")

  case object CounterPage extends TitledPage("Counter")

  case object TimePage extends TitledPage("Time")

  case object FormStatePage extends TitledPage("Form State & Validation")

  case object UncontrolledInputsPage extends TitledPage("Uncontrolled Inputs")

  case object ControlledInputsPage extends TitledPage("Controlled Inputs")

  // This page does not extend TitledPage so that we don't
  // auto-render h1 for it, because it has its own styling
  case object TodoMvcPage extends Page("TodoMVC")

  case class WeatherGradientPage(gradientId: String) extends Page(s"Weather Gradient") // #TODO update title to match content
  
  case object UI5WebComponentsPage extends Page("SAP UI5 Web Components")
  
  case object NotFoundPage extends TitledPage("Page not found")

  // This page does not have a route defined for it, to show you what happens in those cases.
  case class UnroutedPage(foo: String) extends TitledPage(s"Unrouted page")

  given pageCodec: Codec[Page] = deriveAllCodecs

}
