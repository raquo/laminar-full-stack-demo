package com.raquo.app

import com.raquo.app.JsRouter.*
import com.raquo.app.basic.*
import com.raquo.app.form.*
import com.raquo.app.integrations.*
import com.raquo.app.pages.*
import com.raquo.app.todomvc.TodoMvcApp
import com.raquo.app.weather.WeatherGradientView
import com.raquo.laminar.api.L.{*, given}
import com.raquo.waypoint.SplitRender

// BEGIN[waypoint/views]
val views: Signal[HtmlElement] =
  JsRouter.currentPageSignal
    .splitMatchOne
    .handleValue(HomePage)(HomePageView())
    .handleValue(HelloWorldPage)(HelloWorldView())
    .handleValue(CounterPage)(CounterView())
    .handleValue(TimePage)(TimeView())
    .handleValue(UncontrolledInputsPage)(UncontrolledInputsView())
    .handleValue(ControlledInputsPage)(ControlledInputsView())
    .handleValue(FormStatePage)(FormStateView())
    .handleValue(TodoMvcPage)(TodoMvcApp.node)
    .handleType[WeatherGradientPage] { (initialPage, pageSignal) => WeatherGradientView(pageSignal) }
    .handleValue(UI5WebComponentsPage)(SapUI5WebComponentsView())
    .handleValue(ShoelaceWebComponentsPage)(ShoelaceWebComponentsView())
    .handleValue(NetworkRequestsPage)(NetworkRequestsView())
    .handleValue(WaypointRoutingPage)(WaypointRoutingView())
    .handleValue(LocalStoragePage)(LocalStorageView())
    .handleValue(CodeSnippetsPage)(CodeSnippetsView())
    .handleValue(NotFoundPage)(renderNotFoundPage())
    // .handleType[UnroutedPage] { (_, _) => ??? } // commented out to demonstrate what happens if you forget to specify a route
    .toSignal // Yes, "match may not be exhaustive" is expected here. See above.
// END[waypoint/views]

// Old / Scala 2 way of doing the same. This is unused, only for reference:
// BEGIN[waypoint/views-old]
lazy val altViews =
  SplitRender(JsRouter.currentPageSignal)
    .collectStatic(HomePage)(HomePageView())
    .collectStatic(HelloWorldPage)(HelloWorldView())
    .collectStatic(CounterPage)(CounterView())
    .collectStatic(TimePage)(TimeView())
    .collectStatic(UncontrolledInputsPage)(UncontrolledInputsView())
    .collectStatic(ControlledInputsPage)(ControlledInputsView())
    .collectStatic(FormStatePage)(FormStateView())
    .collectStatic(TodoMvcPage)(TodoMvcApp.node)
    .collectSignal[WeatherGradientPage](WeatherGradientView(_))
    .collectStatic(UI5WebComponentsPage)(SapUI5WebComponentsView())
    .collectStatic(ShoelaceWebComponentsPage)(ShoelaceWebComponentsView())
    .collectStatic(NetworkRequestsPage)(NetworkRequestsView())
    .collectStatic(LocalStoragePage)(LocalStorageView())
    .collectStatic(WaypointRoutingPage)(WaypointRoutingView())
    .collectStatic(CodeSnippetsPage)(CodeSnippetsView())
    .collectStatic(NotFoundPage)(renderNotFoundPage())
    .signal
// END[waypoint/views-old]

// All the other page Views are defined in different files
// for easier organization, but of course you can just use
// plain functions like this too.
private def renderNotFoundPage(): HtmlElement = {
  div(
    p("The Waypoint frontend router could not match this URL to any of the routes, so it is rendering the fallback page (NotFoundPage) instead."),
    p("OR – maybe you directly asked Waypoint to render NotFoundPage, e.g. if the URL format was correct but the provided params in the URL were invalid."),
    p("The important part being, it's not the server giving you a 404. The server loaded index.html and that loaded your frontend code, and this frontend code is what's showing this page.")
  )
}
