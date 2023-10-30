package com.raquo.app

import com.raquo.app.pages.*
import com.raquo.waypoint.*

// -- Routes --

// Note: For every URL covered by these routes, the backend should
// serve the index.html file that loads your application.
// In Server.scala, we do this for `/` and for every URL under `/app`.

val appRoot = root / "app"

// Note: there is also NotFoundPage, but it does not have a designated
// URL of its own (like /404): we retain the URL that the user entered to
// give the user a chance to fix it.
// And so, even though NotFoundPage does not have its own route listed
// here, JsRouter.currentPageSignal still emits it when necessary.

val routes = List(
  Route.static(HomePage, root / endOfSegments),
  Route.static(HelloWorldPage, root / "basic" / "hello" / endOfSegments),
  Route.static(CounterPage, root / "basic" / "counter" / endOfSegments),
  Route.static(TimePage, root / "basic" / "time" / endOfSegments),
  Route.static(UncontrolledInputsPage, root / "form" / "uncontrolled-inputs" / endOfSegments),
  Route.static(ControlledInputsPage, root / "form" / "controlled-inputs" / endOfSegments),
  Route.static(FormStatePage, root / "form" / "form-state" / endOfSegments),
  Route.static(TodoMvcPage, root / "apps" / "todomvc" / endOfSegments),
  Route.static(UI5WebComponentsPage, root / "integrations" / "web-components" / "sap-ui5" / endOfSegments),
  Route.static(ShoelaceWebComponentsPage, root / "integrations" / "web-components" / "shoelace" / endOfSegments),
  Route[WeatherGradientPage, String](
    encode = page => page.gradientId,
    decode = gradientId => WeatherGradientPage(gradientId = gradientId),
    pattern = appRoot / "weather" / "gradient" / segment[String] / endOfSegments
  )
)
