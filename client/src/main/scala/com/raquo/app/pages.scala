package com.raquo.app

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

object pages {

  // BEGIN[waypoint/pages]
  sealed trait Page(val title: String)

  // In our code extending this trait means we auto-render an
  // h1 element for this page. I don't necessarily recommend this exact
  // pattern, it's just an example of using the information in the pages
  // for something other than actual URL routing.
  sealed abstract class TitledPage(title: String) extends Page(title)

  case object HomePage extends Page("Laminar Demo")

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
  
  case object ShoelaceWebComponentsPage extends Page("Shoelace Web Components")

  case object NetworkRequestsPage extends Page("Network Requests")

  case object WaypointRoutingPage extends Page("Waypoint URL Routing")

  case object CodeSnippetsPage extends Page("Code Snippets")

  case object NotFoundPage extends TitledPage("Page Not Found")

  // This page does not have a route defined for it, to show you what happens in those cases.
  case class UnroutedPage(foo: String) extends TitledPage(s"Unrouted Page")

  // The information encoded by this codec for any given page will be
  // saved into the browser's History API navigation record, and it will
  // be retrieved if you e.g. use the browser back button to navigate to
  // this page. Keep this in mind when choosing what data to store in the
  // Page trait. You generally don't want to store fat data models here,
  // preferring identifiers like companyId instead (because if company name
  // updates, the data you saved here will get stale). On the other hand,
  // you might want to store things like last scroll position for certain
  // types of pages, and restore it when navigating back to that page.
  given pageCodec: Codec[Page] = deriveAllCodecs
  // END[waypoint/pages]
}
