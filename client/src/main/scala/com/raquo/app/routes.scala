package com.raquo.app

import com.raquo.app.pages.*
import com.raquo.waypoint.*

// -- Routes --

// Note: For every URL covered by these routes, the backend should
// serve the index.html file that loads your application.
// In Server.scala, we do this for `/` and for every URL under `/app`.

// Note: there is also NotFoundPage, but it does not have a designated
// URL of its own (like /404): we retain the URL that the user entered to
// give the user a chance to fix it.
// And so, even though NotFoundPage does not have its own route listed
// here, JsRouter.currentPageSignal still emits it when necessary.

// BEGIN[waypoint/routes]
val appRoot = root / "app"

val routes = List(
  Route.static(HomePage, root / endOfSegments),
  Route.static(HelloWorldPage, appRoot / "basic" / "hello" / endOfSegments),
  Route.static(CounterPage, appRoot / "basic" / "counter" / endOfSegments),
  Route.static(TimePage, appRoot / "basic" / "time" / endOfSegments),
  Route.static(UncontrolledInputsPage, appRoot / "form" / "uncontrolled-inputs" / endOfSegments),
  Route.static(ControlledInputsPage, appRoot / "form" / "controlled-inputs" / endOfSegments),
  Route.static(FormStatePage, appRoot / "form" / "form-state" / endOfSegments),
  Route.static(TodoMvcPage, appRoot / "apps" / "todomvc" / endOfSegments),
  Route.static(UI5WebComponentsPage, appRoot / "integrations" / "web-components" / "sap-ui5" / endOfSegments),
  Route.static(ShoelaceWebComponentsPage, appRoot / "integrations" / "web-components" / "shoelace" / endOfSegments),
  Route.static(NetworkRequestsPage, appRoot / "integrations" / "network-requests" / endOfSegments),
  Route.static(WaypointRoutingPage, appRoot / "integrations" / "waypoint-url-routing" / endOfSegments),
  Route.static(CodeSnippetsPage, appRoot / "integrations" / "code-snippets" / endOfSegments),
  Route[WeatherGradientPage, String](
    encode = page => page.gradientId,
    decode = gradientId => WeatherGradientPage(gradientId = gradientId),
    pattern = appRoot / "weather" / "gradient" / segment[String] / endOfSegments
  )
)
// END[waypoint/routes]
