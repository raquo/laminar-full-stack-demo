package com.raquo.app.features

import com.raquo.app.JsRouter
import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.app.codesnippets.generated.GeneratedSnippets
import com.raquo.app.pages.MathMlPage
import com.raquo.laminar.api.L.*
import com.raquo.laminar.api.features.unitArrows

object SvgView {

  def apply(): HtmlElement = {
    div(
      h1("SVG"),
      p(b("⚠\uFE0F Security warning:"), " SVGs are not safe like other image formats. Just like HTML, SVG can contain arbitrary JS code, which can easily get executed in the context of your website, leading to vulnerabilities. Avoid rendering SVGs from untrusted sources. Learn about XSS attacks – this is not about Laminar, it's fundamental to frontend development. This page is not a comprehensive security tutorial."),
      p("There are a few ways to render SVGs in the browser. Most common are:"),
      //
      //
      //
      h2("From external URL"),
      p("If you have your SVG file on a public URL, you can render it with the usual <img> tag. In this case, the browser treats the SVG just as any other image, so you don't get to fiddle with its internals, but you can still add props / attributes / styles / events to the <img> element itself."),
      // BEGIN[svg/external]
      img(
        width.px(150),
        src("/assets/smile.svg"),
        alt("Smile svg image"),
        color.red, // no effect – can only change colors in the SVG source.
        fill("#ccc") // no effect – can only change colors in the SVG source.
      ),
      // END[svg/external]
      CodeSnippets(_.`svg/external`),
      p("When referencing the SVG by URL like this, it's not downloaded until you actually render it."),
      p("And the scripts in this SVG are ", b("not"), " executed – this SVG actually has an alert() JS code in it, and yet you don't see it. ", b("However, "), "if you open the SVG URL as its own page in the browser, the scripts embedded in it ", b("WILL"), " run – it's just like opening an HTML file. To demonstrate, click on this ", a(href("/assets/smile.svg"), "smile.svg"), " link – once you open that URL, the SVG will use JS to show you an alert(), but it could have executed any evil code that way. Lots of ways for an attacker to exploit this."),
      p("So, you should only use trusted SVGs. You can also sanitize SVGs, perhaps using, ", a(href("https://github.com/cure53/DOMPurify"), "DomPurify"), ", to remove dangerous code – more on that later (TODO)"),
      //
      //
      //
      h2("Inline SVG from a string"), {
        // BEGIN[svg/string]
        val flipVar = Var(false)
        p(
          foreignSvgElement(DomApi.unsafeParseSvgString(svgSmileString)).amend(
            width.px(150),
            color <-- flipVar.signal.map(if (_) "red" else "#000"),
            fill <-- flipVar.signal.map(if (_) "#ccc" else "#ffd700"),
            onClick --> flipVar.update(!_),
            cursor.pointer,
          )
        )
        // END[svg/string]
      },
      CodeSnippets(_.`svg/string`, startExpanded = _ == GeneratedSnippets.`svg/string`.head),
      p("If you have your SVG in a string, you can render it directly in Laminar in two steps: first, ", code("DomApi.unsafeParseSvgString(svgString)"), " parses your string into a native JS SVG element. And then, Laminar's ", code("foreignSvgElement"), " method mounts this native JS element into the tree of Laminar elements."),
      p("This way, your SVG becomes part of the DOM of this webpage. You can therefore configure it more thoroughly, for example, this smiley looks less friendly than usual because we're setting `color` and `fill` on it. The `color` is picked up by `currentColor` in the SVG, and the provided `fill` overrides the root `fill` defined in the SVG, ultimately being applied to the only SVG element that does not have `fill` explicitly defined. Remember that most CSS styles are inherited like this."),
      p("These styling techniques are commonly used to adjust SVG icon colors when using third party SVG icon sets."),
      p("⚠\uFE0F You should not render untrusted SVGs this way. Even though the browser does not execute our onload script, it does execute the onclick script that's found in the SVG string. Try clicking on the SVG."),
      //
      //
      //
      h2("SVG using Laminar element API"), {
        // BEGIN[svg/element]
        val flipVar = Var(false)
        p(
          svgSmileElement(
            faceMods = {
              fill <-- flipVar.signal.map(if (_) "#ccc" else "#ffd700")
            },
            leftEyeCenterMods = {
              fill <-- flipVar.signal.map(if (_) "red" else "#000")
            }
          ).amend(
            width.px(150),
            onClick --> flipVar.update(!_),
            cursor.pointer,
          )
        )
        // END[svg/element]
      },
      CodeSnippets(_.`svg/element`, startExpanded = _ == GeneratedSnippets.`svg/element`.head),
      p("Aside from rendering SVGs from pre-made SVG strings, you can of course author SVG documents with Laminar similarly to how you're authoring HTML documents. This means you don't need to depend on limited configuration via `currentColor` and `fill` inheritance, and can do whatever you need however you want."),
      p("In this `def svgSmileElement` snippet we use SVG tags / attrs with the `svg.` prefix, but if you're authoring a large SVG, you can also import all SVG terms similarly to how we do it on the ", a(JsRouter.navigateTo(MathMlPage), "MathML page"), "."),
      h2("Documentation:"),
      ul(
        li(a(href("https://laminar.dev/documentation#svg"), "SVG")),
        li(a(href("https://laminar.dev/documentation#rendering-third-party-html-or-svg-elements"), "Rendering Third Party HTML or SVG Elements")),
        li(a(href("https://laminar.dev/documentation#parsing-html-or-svg-strings-into-elements"), "Parsing HTML or SVG Strings into Elements")),
      ),
    )
  }

  // BEGIN[svg/string]
  private def svgSmileString: String = {
    """<svg width="200"
      |     height="200"
      |     viewBox="0 0 200 200"
      |     xmlns="http://www.w3.org/2000/svg"
      |     onload="setTimeout(x => alert('Demo message from smile.svg. Example XSS attack.'), 500)"
      |     onclick="alert('Hello XSS (demo)')"
      |     fill="#FFD700"
      |>
      |  <!-- Yellow circle face. Fill color inherited from root svg element. -->
      |  <circle cx="100" cy="100" r="70" stroke="#000" stroke-width="2" />
      |
      |  <!-- Left eye -->
      |  <circle cx="75" cy="85" r="10" fill="#000" />
      |
      |  <!-- Left eye center (currentColor is black by default, but can be set externally) -->
      |  <circle cx="75" cy="85" r="3" fill="currentColor" />
      |
      |  <!-- Right eye -->
      |  <circle cx="125" cy="85" r="10" fill="#000" />
      |
      |  <!-- Smile (proper circular arc, 40% larger) -->
      |  <path d="M 58 115 A 50 50 0 0 0 142 115"
      |        stroke="#000"
      |        stroke-width="3"
      |        fill="none"
      |        stroke-linecap="round" />
      |</svg>""".stripMargin
  }
  // END[svg/string]

  // BEGIN[svg/element]
  private def svgSmileElement(
    faceMods: Modifier[SvgElement],
    leftEyeCenterMods: Modifier[SvgElement],
  ): SvgElement = {
    svg.svg(
      svg.width("200"),
      svg.height("200"),
      svg.viewBox("0 0 200 200"),
      // Face
      svg.circle(
        svg.cx("100"),
        svg.cy("100"),
        svg.r("70"),
        svg.fill("#FFD700"),
        svg.stroke("#000"),
        svg.strokeWidth("2"),
        faceMods
      ),
      // Left eye
      svg.circle(
        svg.cx("75"),
        svg.cy("85"),
        svg.r("10"),
        svg.fill("#000")
      ),
      // Left eye center
      svg.circle(
        svg.cx("75"),
        svg.cy("85"),
        svg.r("3"),
        svg.fill("#000"),
        leftEyeCenterMods
      ),
      // Right eye
      svg.circle(
        svg.cx("125"),
        svg.cy("85"),
        svg.r("10"),
        svg.fill("#000")
      ),
      // Smile
      svg.path(
        svg.d("M 58 115 A 50 50 0 0 0 142 115"),
        svg.stroke("#000"),
        svg.strokeWidth("3"),
        svg.fill("none"),
        svg.strokeLineCap("round")
      )
    )
  }
  // END[svg/element]
}
