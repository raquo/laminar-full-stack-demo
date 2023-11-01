package com.raquo.app

import com.raquo.app.JsRouter.*
import com.raquo.buildinfo.BuildInfo
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect
import com.raquo.weather.Gradient
import com.raquo.weather.Gradient.Squamish

object HomePageView {

  JSImportSideEffect("@find/**/HomePageView.less")

  // Shared inline styles. You can also use CSS classes for common styling of course.
  // See https://laminar.dev/documentation#approaches-to-css for a high level summary.
  private val listStyles = List(
    margin := "10px 0",
    paddingLeft := "30px",
    lineHeight := style.em(1.75) // typed helper to build "1.75em" string.
  )

  private val basicPages = List(HelloWorldPage, CounterPage, TimePage)

  private val formPages = List(UncontrolledInputsPage, ControlledInputsPage, FormStatePage)

  def apply(): HtmlElement = {
    div(
      cls("HomePageView"),
      h1(
        img(cls("-logo"), src("https://laminar.dev/img/brand/laminar-logo-200px-rounded.png")),
        "Laminar Demo"
      ),
      p("Welcome to ", a(href("https://laminar.dev"), "Laminar"), " & ", a(href("https://www.scala-js.org/"), "Scala.js"), " full stack demo. The ", a(href("https://github.com/raquo/laminar-full-stack-demo"), "source code & README"), " are on Github."),
      h2("Basic examples"),
      ul(
        listStyles,
        basicPages.map(pageLink(_))
      ),
      h2("Forms"),
      ul(
        listStyles,
        formPages.map(pageLink(_))
      ),
      h2("Apps"),
      ul(
        listStyles,
        pageLink(TodoMvcPage, caption = Some("TodoMVC (state management)")),
        pageLink(WeatherGradientPage(Squamish.id), caption = Some("Wind gradient (chart.js, JSON, backend)"))
      ),
      h2("Integrations"),
      ul(
        listStyles,
        pageLink(UI5WebComponentsPage),
        pageLink(ShoelaceWebComponentsPage),
        pageLink(NetworkRequestsPage, caption = Some("Network requests (Fetch & Ajax)")),
        pageLink(WaypointRoutingPage),
        pageLink(CodeSnippetsPage, caption = Some("Code snippets (highlight.js + compile-time data)"))
      ),
      h2("Broken links for testing"),
      ul(
        listStyles,
        pageLink(
          WeatherGradientPage("foo"),
          caption = Some("Wind gradient with bad gradientId")
        ),
        // Note: the below is commented out by default to prevent annoying errors in the console.
        //
        // Note: the page linked below is unroutable, so our `navigateTo` helper can't generate
        // a URL for it, and so its <a> element has no `href` property. By default, the browsers
        // make such href-less links appear like plaintext & unclickable, but that's just styling.
        // You can still click on it, and get an exception. And you can override the styles with CSS.
        //{
        //  dom.console.warn("The code below will print a Waypoint error about UnroutedPage(bar) to the console. This is expected, we are demonstrating this failure on purpose.")
        //  pageLink(
        //    UnroutedPage("bar"),
        //    caption = Some("UnroutedPage – page with no route")
        //  )
        //}
      ),
      br(),
      br(),
      hr(),
      p(small(s"Built with Laminar v${BuildInfo.laminarVersion}")),
    )
  }

  private def pageLink(page: Page, caption: Option[String] = None): HtmlElement = {
    li(a(
      navigateTo(page),
      caption.getOrElse(page.title)
    ))
  }

}
