package com.raquo.app.integrations

import com.raquo.app.JsRouter.titleLink
import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect
import org.scalajs.dom
import vendor.shoelace.Shoelace
import vendor.shoelace.components.{Button, Icon, Switch}

object ShoelaceWebComponentsView {

  JSImportSideEffect("@find/**/ShoelaceWebComponentsView.less")

  // Load Shoelace themes. Light one is the default, but we make a button to switch them.
  // See their contents at https://github.com/shoelace-style/shoelace/blob/current/src/themes/light.css
  // BEGIN[shoelace/themes]
  JSImportSideEffect("@shoelace-style/shoelace/dist/themes/light.css")
  JSImportSideEffect("@shoelace-style/shoelace/dist/themes/dark.css")
  // END[shoelace/themes]

  // This path is determined by `dest` config of `rollupCopyPlugin` in vite.config.js
  // Note: This needs to be called once, prior to loading any Shoelace components
  //   JsApp would be a good place to put this, I'm just putting it here because
  //   in this demo project, this is the only view that uses Shoelace components.
  Shoelace.setBasePath("/assets/shoelace")

  def apply(): HtmlElement = {
    // BEGIN[shoelace/themes]
    val isDarkVar = Var(false)
    // END[shoelace/themes]

    div(
      cls("ShoelaceWebComponentsView"),

      h1("Shoelace Web Components"),
      p(a(href("https://shoelace.style/"), "Shoelace"), " is a well made library of modern looking Web Components. Unlike SAP UI5, we don't yet have Scala.js bindings for this library. I have created bindings for several Shoelace components shown here just for this demo project, and so far I like it more than UI5. If there's enough demand, I can make bindings for all of Shoelace components, but I don't know when I will have the time."),

      CodeSnippets(
        _.`shoelace/components`.sortBy {
          case s if s.fileName.endsWith("Button.scala") => 1
          case s if s.fileName.endsWith("Icon.scala") => 2
          case s if s.fileName.endsWith("Switch.scala") => 3
          case s if s.fileName.endsWith("WebComponent.scala") => 4
          case s if s.fileName.endsWith("CommonTypes.scala") => 5
          case _ => 10
        },
        caption = "Source code of my Shoelace interfaces used below, for reference:",
        asParagraph = true,
        startExpanded = _ => false
      ),

      h2(titleLink("buttons-icons"), "Buttons and Icons"),
      p(
        // BEGIN[shoelace/buttons-and-icons]
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
            Icon.of(_.name("arrow-counterclockwise"))
          )
        ),
        " ",
        Button.of(
          _.variant.success,
          _ => "User",
          _ => onClick --> { _ => dom.window.alert("Clicked") },
          _.slots.suffix(
            Icon.of(_.name("person-fill"))
          )
        )
        // END[shoelace/buttons-and-icons]
      ),
      p("Icons and their names are from ", a(href("https://icons.getbootstrap.com"), "Bootstrap Icons"), " by default. To find available icons, create a search engine bookmark in your browser with keyword ", code("bs"), " and URL ", code("https://icons.getbootstrap.com/?q=%s"), ", then you'll be able to type e.g. \"bs user\" in your address bar, and see all relevant icons and their names."),
      CodeSnippets(_.`shoelace/buttons-and-icons`),

      h1("Customization"),

      p("These methods and examples largely follow ", a(href("https://shoelace.style/getting-started/customizing"), "Shoelace customization docs"), "."),

      h2(titleLink("themes"), "Using themes"),
      // BEGIN[shoelace/themes]
      cls <-- isDarkVar.signal.map(if (_) "sl-theme-dark" else "sl-theme-light"),
      // END[shoelace/themes]
      // BEGIN[shoelace/themes]
      Button.of(
        _.variant.primary,
        _ => child.text <-- isDarkVar.signal.map(if (_) "Use light theme" else "Use dark theme"),
        _ => onClick.mapTo(!isDarkVar.now()) --> isDarkVar,
        _.slots.prefix(
          Icon.of(
            _.name <-- isDarkVar.signal.map(if (_) "brightness-high-fill" else "moon-stars-fill"),
            _.fontSize.em(1.3), // this is how you set icon size in shoelace
          )
        )
      ),
      // END[shoelace/themes]
      p("The button above switches this page between light and dark themes. The themes style the Shoelace components that we render here, but I also set the color and background color of this page by referring to the CSS vars that the active theme sets."),
      p("See ", a(href("https://shoelace.style/getting-started/themes"), "Shoelace theme docs"), " for instructions on using themes, creating your own themes, and loading multiple themes in the same app."),
      CodeSnippets(_.`shoelace/themes`),

      h2(titleLink("design-tokens"), "Using design tokens"),
      // BEGIN[shoelace/design-tokens]
      p(
        cls("indigoPrimaryColor"), // you could also apply this class directly to the button.
        Button.of(
          _.variant.primary,
          _ => "Primary indigo",
          _ => onClick --> { _ => dom.window.alert("Clicked") }
        )
      ),
      // END[shoelace/design-tokens]
      p("Shoelace theme defines \"design tokens\", which are just CSS custom properties. These properties are inherited, so you can override those either globally or only in a certain CSS scope. The button above is rendered using \"primary\" style, but we overrode the primary colors to be indigo instead of the default sky blue."),
      CodeSnippets(_.`shoelace/design-tokens`),

      h2(titleLink("css-parts"), "Using CSS parts"),
      p(
        // BEGIN[shoelace/css-parts]
        Button.of(
          _ => cls("tomato-button"),
          _ => "Tasteful tomato button",
          _ => onClick --> { _ => dom.window.alert("Clicked") },
          _.slots.prefix(
            Icon.of(_.name("check-circle-fill"))
          )
        ),
        " ",
        Button.of(
          _ => cls("pink"),
          _ => "Crazy pink button",
          _ => onClick --> { _ => dom.window.alert("Clicked") }
        ),
        // END[shoelace/css-parts]
        CodeSnippets(_.`shoelace/css-parts`)
      ),

      h2(titleLink("css-custom-properties"), "Using CSS custom properties"),
      p(
        // #TODO[IJ] Why isn't the right `Switch` offered for import here? File a bug report after publishing the repo.
        // BEGIN[shoelace/css-custom-properties]
        Switch.of(),
        " ",
        Switch.of(
          _.width.px(100),
          _.height.px(10),
          _.thumbSize.px(14)
        ),
        // END[shoelace/css-custom-properties]
        CodeSnippets(_.`shoelace/css-custom-properties`)
      ),
      p("Above is a standard ", a(href("https://shoelace.style/components/switch"), "Switch"), " component, followed by a Switch that is custom-sized using custom CSS properties that this web component exposes."),
    )
  }
}
