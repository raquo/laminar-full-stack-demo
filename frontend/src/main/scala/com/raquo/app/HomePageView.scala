package com.raquo.app

import com.raquo.app.JsRouter.*
import com.raquo.laminar.api.L.{*, given}
import com.raquo.weather.Gradient
import org.scalajs.dom

object HomePageView {

  // Shared inline styles. You can also use CSS classes for common styling of course.
  // See https://laminar.dev/documentation#approaches-to-css for a high level summary.
  private val linkStyles = List(
    lineHeight := style.em(1.75),
    paddingLeft := "30px"
  )

  private val gradientPages = Gradient.values.toList.map { gradient =>
    (gradient.name, WeatherGradientPage(gradient.id))
  }

  private val basicPages = List(HelloWorldPage, CounterPage, FormStatePage)

  def apply(): HtmlElement = {
    div(
      //cls("HomePageView"),
      h2("Basic examples"),
      ul(
        linkStyles,
        basicPages.map(pageLink(_))
      ),
      h2("Wind gradient (chart.js)"),
      ul(
        linkStyles,
        gradientPages.map { (caption, page) =>
          pageLink(page, caption = Some(caption))
        }
      ),
      h2("Broken links for testing"),
      ul(
        linkStyles,
        pageLink(
          WeatherGradientPage("foo"),
          caption = Some("Weather gradient with bad gradientId")
        ),
        // Note: the page linked below is unroutable, so our `navigateTo` helper can't generate
        // a URL for it, and so its <a> element has no `href` property. By default, the browsers
        // make such href-less links appear like plaintext & unclickable, but that's just styling.
        // You can still click on it, and get an exception. And you can override the styles with CSS.
        {
          dom.console.warn("The code below will print a Waypoint error about UnroutedPage(bar) to the console. This is expected, we are demonstrating this failure on purpose.")
          pageLink(
            UnroutedPage("bar"),
            caption = Some("UnroutedPage – page with no route")
          )
        }
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
