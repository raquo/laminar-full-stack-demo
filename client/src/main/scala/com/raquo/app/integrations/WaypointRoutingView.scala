package com.raquo.app.integrations

import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.laminar.api.L.*

object WaypointRoutingView {

  def apply(): HtmlElement = {
    div(
      h1("Waypoint URL Routing"),
      p("As you navigate this whole \"Laminar Demo\" app, notice that the URL updates to reflect the page you're on. However, navigation happens instantly, without reloading the entire web page from the server. This is client-side URL routing, part of what is known as \"single page application\" (SPA) architecture."),
      p("To achieve this, we use ", a(href("https://github.com/raquo/Waypoint"), "Waypoint"), ", my router for Laminar. Yurique's ", a(href("https://github.com/tulz-app/frontroute"), "Frontroute"), "is another alternative, but here I will explain how this demo app uses Waypoint."),
      p("Before reading further, please read the short ", a(href("https://github.com/raquo/Waypoint#routing-basics"), "Routing Basics"), " section of Waypoint docs to understand Waypoint's terminology."),
      hr(),
      p("So, first, we define the pages. Waypoint wants you to have a common Page trait, with individual pages expressed in an ADT hierarchy. And yes, intermediate and marker traits are supported."),
      CodeSnippets(_.`waypoint/pages`, asParagraph = true),
      p("Then, we define routes – linking those pages to URLs at which they will appear:"),
      CodeSnippets(_.`waypoint/routes`, asParagraph = true),
      p("Most of those are static, but the last one is dynamic, and it defines how to parse a certain type of URLs into a certain type of pages, and how to build such URLs from such pages. Waypoint uses Antoine's ", a(href("https://github.com/sherpal/url-dsl"), "URL-DSL"), " library for the route patterns."),
      p("Having pages and routes, we can now create a Router, which is what sets the current URL to match the page, listens to the browser back button clicks, etc. We also added a couple navigation-related helpers to it."),
      CodeSnippets(_.`waypoint/router`, asParagraph = true),
      p("The router exposes currentPageSignal, which we use in a few different ways. First, just to render the \"Back to home\" button on every page that isn't home page:"),
      CodeSnippets(_.`waypoint/currentPageSignal/backToHome`, asParagraph = true),
      p("But the real meat is rendering all the elements corresponding to those pages. We call those \"views\", and we define them in a separate file:"),
      CodeSnippets(_.`waypoint/views`, asParagraph = true),
      p("Everything that ends in \"View()\" here just returns a Laminar element. One notable view is WeatherGradientView – because it's linked to a dynamic route, Waypoint provides it with a ", code("Signal[WeatherGradientPage]"), " – the exact type that it needs to render its contents efficiently. Note that thanks to all this setup that we did, Waypoint essentially ", code("collect"), "-ed the router's ", code("currentPageSignal"), ", which is a ", code("Signal[Page]"), ", into a ", code("Signal[WeatherGradientPage]"), " for your convenience. In the absense of virtual DOM, it's actually very important for efficiency to have a pattern like this, but you'll need to read much more Laminar & Airstream docs to fully appreciate it."),
      p("Finally, back in JsApp.scala, we render the view corresponding to the current URL:"),
      CodeSnippets(_.`waypoint/currentPageSignal/view`, asParagraph = true),
      p("Alright, so, what do you need to do if you want to add a new page?"),
      ol(
        li("Create a Page to define the data that will be stored in the history record"),
        li("Create a Route to define the URL pattern, and add it to the list of routes"),
        li("Create a View and link it to render for that page"),
      ),
      p("To create a link that will navigate to a given page on click, you can do this:", code("a(JsRouter.navigateTo(HomePage), \"Home page\")"), "."),
      p("To actually trigger navigation to any page, just say ", code("JsRouter.pushState(HomePage)"), "."),
      p("This covers the basics, but of course there's much more to learn. Importantly, in your own project, make sure to set up the backend's routing to work in tandem with Waypoint. I did this here for http4s (see Server.scala), so dig in the code of this demo, and don't forget to check out Waypoint docs!"),
    )
  }
}
