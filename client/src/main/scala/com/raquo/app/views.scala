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
val views = SplitRender(JsRouter.currentPageSignal)
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
// END[waypoint/views]

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
