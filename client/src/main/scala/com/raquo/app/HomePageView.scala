package com.raquo.app

import com.raquo.app.JsRouter.*
import com.raquo.laminar.api.L.{*, given}
import com.raquo.weather.Gradient
import com.raquo.weather.Gradient.Squamish
import org.scalajs.dom

object HomePageView {

  // Shared inline styles. You can also use CSS classes for common styling of course.
  // See https://laminar.dev/documentation#approaches-to-css for a high level summary.
  private val listStyles = List(
    margin := "10px 0",
    paddingLeft := "30px",
    lineHeight := style.em(1.75) // typed helper to build "1.75em" string.
  )

  private val basicPages = List(HelloWorldPage, CounterPage, TimePage)

  private val formPages = List(UncontrolledInputsPage, ControlledInputsPage, FormStatePage)

  private val appPages = List(TodoMvcPage)

  def apply(): HtmlElement = {
    div(
      //cls("HomePageView"),
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
        appPages.map(pageLink(_))
      ),
      h2("Integrations"),
      ul(
        listStyles,
        pageLink(WeatherGradientPage(Squamish.id), caption = Some("Wind gradient (chart.js)")),
        pageLink(UI5WebComponentsPage),
        pageLink(ShoelaceWebComponentsPage),
        li(">>> Fetch Tester"),
        li(">>> SVG files, inline, etc.")
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
      )
    )
  }

  private def pageLink(page: Page, caption: Option[String] = None): HtmlElement = {
    li(a(
      navigateTo(page),
      caption.getOrElse(page.title)
    ))
  }

}
