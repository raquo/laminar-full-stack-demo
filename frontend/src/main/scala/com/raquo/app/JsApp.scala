package com.raquo.app

import com.raquo.app.JsRouter.*
import com.raquo.app.weather.WeatherGradientView
import com.raquo.laminar.api.L.{*, given}
import com.raquo.waypoint.*
import com.raquo.weather.Gradient
import org.scalajs.dom

import scala.scalajs.js

object JsApp {

  // This method is the entry point of your JS app.
  // It is recognized by its name and type signature,
  // do not rename it.
  def main(args: Array[String]): Unit = {
    println("-- Scala.js app start --")

    // Find the div to render the app into. It's defined in index.html
    lazy val container = dom.document.getElementById("root")

    lazy val appElement = {
      div.apply(
        child.maybe <-- JsRouter.currentPageSignal.map {
          case HomePage => None
          case _ => Some(h3(a(navigateTo(HomePage), "Back to home")))
        },
        child <-- selectedAppSignal,
      )
    }
    
    render(container, appElement)
  }


  private val selectedAppSignal = SplitRender(JsRouter.currentPageSignal)
    .collectStatic(HomePage)(renderHomePage())
    .collectSignal[WeatherGradientPage](WeatherGradientView(_))
    .collectStatic(NotFoundPage)(renderNotFoundPage())
    .signal

  private def renderHomePage(): HtmlElement = {
    // Shared inline styles. You can also use CSS classes for common styling of course.
    // See https://laminar.dev/documentation#approaches-to-css for a high level summary.
    val linkStyles = List(
      fontSize := "120%",
      lineHeight := "2em",
      listStyleType.none,
      paddingLeft := "0px"
    )

    div(
      h1("Laminar Demo"),
      ul(
        linkStyles,
        linkPages.map { (caption, page) =>
          li(a(navigateTo(page), caption))
        }
      ),
      h2("Broken links for testing"),
      ul(
        linkStyles,
        li(a(navigateTo(WeatherGradientPage("foo")), "Weather gradient with bad gradientId")),
        // Note: the page linked below is unroutable, so our `navigateTo` helper can't generate
        // a URL for it, and so its <a> element has no `href` property. By default, the browsers
        // make such href-less links appear like plaintext & unclickable, but that's just styling.
        // You can still click on it, and get an exception. And you can override the styles with CSS.
        {
          dom.console.warn("The code below will print a Waypoint error about UnroutedPage(bar) to the console. This is expected, we are demonstrating this failure on purpose.")
          li(a(navigateTo(UnroutedPage("bar")), "UnroutedPage – page with no route"))
        }
      )
    )
  }

  val linkPages: List[(String, Page)] = {
    val gradientPages = Gradient.values.toList.map { gradient =>
      (gradient.name, WeatherGradientPage(gradient.id))
    }
    gradientPages ++ List()
  }

  private def renderNotFoundPage(): HtmlElement = {
    div(
      h1("Page not found"),
      p("The Waypoint frontend router could not match this URL to any of the routes, so it is rendering the fallback page (NotFoundPage) instead."),
      p("OR – maybe you directly asked Waypoint to render NotFoundPage, e.g. if the URL format was correct but the provided params in the URL were invalid."),
      p("The important part being, it's not the server giving you a 404. The server loaded index.html and that loaded your frontend code, and that code is what's showing this page.")
    )
  }
}
