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
  Route.static(HomePage, root),
  Route.static(HelloWorldPage, appRoot / "basic" / "hello"),
  Route.static(CounterPage, appRoot / "basic" / "counter"),
  Route.static(TimePage, appRoot / "basic" / "time"),
  Route.static(UncontrolledInputsPage, appRoot / "form" / "uncontrolled-inputs"),
  Route.static(ControlledInputsPage, appRoot / "form" / "controlled-inputs"),
  Route.static(FormStatePage, appRoot / "form" / "form-state"),
  Route.static(TodoMvcPage, appRoot / "apps" / "todomvc"),
  Route.static(UI5WebComponentsPage, appRoot / "integrations" / "web-components" / "sap-ui5"),
  Route.static(ShoelaceWebComponentsPage, appRoot / "integrations" / "web-components" / "shoelace"),
  Route.static(NetworkRequestsPage, appRoot / "integrations" / "network-requests"),
  Route.static(LocalStoragePage, appRoot / "integrations" / "localstorage"),
  Route.static(WaypointRoutingPage, appRoot / "integrations" / "waypoint-url-routing"),
  Route.static(CodeSnippetsPage, appRoot / "integrations" / "code-snippets"),
  Route.static(SvgPage, appRoot / "features" / "svg"),
  Route.static(MathMlPage, appRoot / "features" / "math-ml"),
  Route.static(CodeSplittingPage, appRoot / "features" / "code-splitting"),
  Route[WeatherGradientPage, String](
    encode = page => page.gradientId,
    decode = gradientId => WeatherGradientPage(gradientId = gradientId),
    pattern = appRoot / "weather" / "gradient" / segment[String]
  )
)
// END[waypoint/routes]
