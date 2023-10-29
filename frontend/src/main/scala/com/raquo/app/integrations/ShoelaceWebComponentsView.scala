package com.raquo.app.integrations

import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect
import org.scalajs.dom
import vendor.shoelace.Shoelace
import vendor.shoelace.components.{Button, Icon}

import scala.scalajs.js

object ShoelaceWebComponentsView {

  JSImportSideEffect("@find/**/ShoelaceWebComponentsView.less")

  // This path is determined by `dest` config of `rollupCopyPlugin` in vite.config.js
  // Note: This needs to be called once, prior to loading any Shoelace components
  //   JsApp would be a good place to put this, I'm just putting it here because
  //   in this demo project, this is the only view that uses Shoelace components.
  Shoelace.setBasePath("/assets/shoelace")

  def apply(): HtmlElement = {
    div(
      cls("ShoelaceWebComponentsView"),
      h1("Shoelace Web Components"),
      p(a(href("https://shoelace.style/"), "Shoelace"), " is a well made library of modern looking Web Components. Unlike SAP UI5, we don't yet have Scala.js bindings for this library. I have created bindings for several Shoelace components shown here just for this demo project. I would like to make bindings for all of Shoelace components, but I don't know when I will have the time."),
      h2("Buttons and Icons"),
      p(
        Button.of(
          _.variant.primary,
          _.size.large,
          _ => "Settings",
          _ => onClick --> { _ => dom.window.alert("Clicked") },
          _.slots.prefix(
            Icon.of(
              _.name("gear-fill"),
              _.fontSize.em(1.3), // this is how you set icon size in shoelace
            )
          )
        ),
        " ",
        Button.of(
          _ => "Reload",
          _ => onClick --> { _ => dom.window.alert("Clicked") },
          _.slots.prefix(
            Icon.of(
              _.name("arrow-counterclockwise")
            )
          )
        ),
        " ",
        Button.of(
          _.variant.success,
          _ => "User",
          _ => onClick --> { _ => dom.window.alert("Clicked") },
          _.slots.suffix(
            Icon.of(
              _.name("person-fill")
            )
          )
        )
      ),
      p("Icons and their names are from ", a(href("https://icons.getbootstrap.com"), "Bootstrap Icons"), " by default. To find available icons, create a search engine bookmark in your browser with keyword ", code("bs"), " and URL ", code("https://icons.getbootstrap.com/?q=%s"), ", then you'll be able to type e.g. \"bs user\" in your address bar, and see all relevant icons and their names.")
    )
  }
}
