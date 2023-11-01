package com.raquo.app

import com.raquo.app.pages.Page
import com.raquo.app.routes
import com.raquo.laminar.api.L.*
import com.raquo.utils.JsonUtils.*
import com.raquo.waypoint
import io.bullet.borer.*
import org.scalajs.dom

import scala.util.{Failure, Success, Try}

/** See [[https://github.com/raquo/Waypoint Waypoint documentation]] for details on how frontend routing works. */
object JsRouter extends waypoint.Router[Page](
  routes = routes,
  getPageTitle = _.title, // displayed in the browser tab next to favicon
  serializePage = page => Json.encode(page).toUtf8String, // serialize page data for storage in History API log
  deserializePage = pageStr => Json.decodeString(pageStr).to[Page].value, // deserialize the above
  routeFallback = _ => pages.NotFoundPage
)(
  popStateEvents = windowEvents(_.onPopState), // this is how Waypoint avoids an explicit dependency on Laminar
  owner = unsafeWindowOwner // this router will live as long as the window
) {

  // Instead of importing `JsRouter.*` and `pages.*` in your code,
  // you can just import `JsRouter.*` and have both available,
  // since you will be using them together anyway. Neat, eh?
  export com.raquo.app.pages.*

  // Note: this returns a modifier that you need to hang off a Laminar element,
  // e.g. `a(navigateTo(HomePage), "Back to Home")`
  // See https://github.com/raquo/Waypoint docs for why this modifier is useful in general.
  // Note: for fragment ('#') URLs this isn't actually needed.
  def navigateTo(page: Page): Binder[HtmlElement] = Binder { el =>
    val isLinkElement = el.ref.isInstanceOf[dom.html.Anchor]

    if (isLinkElement) {
      Try(absoluteUrlForPage(page)) match {
        case Success(url) => el.amend(href(url))
        case Failure(err) => dom.console.error(err)
      }
    }

    // If element is a link and user is holding a modifier while clicking:
    //  - Do nothing, browser will open the URL in new tab / window / etc. depending on the modifier key
    // Otherwise:
    //  - Perform regular pushState transition
    (onClick
      .filter(ev => !(isLinkElement && (ev.ctrlKey || ev.metaKey || ev.shiftKey || ev.altKey)))
      .preventDefault
      --> (_ => pushState(page))
      ).bind(el)
  }

  /** Add this to a h1..h6 titleelement to add a clickable
    * "#" link that will scroll to that title.
    * `id` is the fragment that will appear in the URL.
    */
  def titleLink(id: String, caption: String = "#"): Modifier.Base = {
    List[Modifier.Base](
      // Sets the id attribute of the title element into which we add this link
      Modifier(parentEl => parentEl.ref.id = id),
      // Creates a link and inserts it into the title
      a(cls("u-titleLink"), href(s"#$id"), caption)
    )
  }
}
