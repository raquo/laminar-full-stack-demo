package com.raquo.app

import com.raquo.app.pages.Page
import com.raquo.app.routes
import com.raquo.laminar.api.L.*
import com.raquo.utils.JsonUtils.*
import com.raquo.waypoint
import io.bullet.borer.*
import org.scalajs.dom

import scala.scalajs.js
import scala.util.{Failure, Success, Try}

// BEGIN[waypoint/router]

/** See [[https://github.com/raquo/Waypoint Waypoint documentation]] for details on how frontend routing works. */
object JsRouter extends waypoint.Router[Page](
  routes = routes,
  getPageTitle = _.title, // displayed in the browser tab next to favicon
  serializePage = page => Json.encode(page).toUtf8String, // serialize page data for storage in History API log
  deserializePage = pageStr => Json.decodeString(pageStr).to[Page].value, // deserialize the above
  routeFallback = _ => pages.NotFoundPage
) {

  // Instead of importing `JsRouter.*` and `pages.*` in your code,
  // you can just import `JsRouter.*` and have both available,
  // since you will be using them together anyway. Neat, eh?
  // #TODO[Scala] Disabled due to Scala Bug https://github.com/scala/scala3/issues/18216#issuecomment-1863659868
  //  - For now, `import com.raquo.app.pages.*` in your code instead
  // export com.raquo.app.pages.*

  currentPageSignal.foreach { page =>
    // Reset scroll position (see Waypoint docs for caveats / more details)
    dom.window.scrollTo(x = 0, y = 0)
  }(owner)

  /** Add this to a h1..h6 title element to add a clickable
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
// END[waypoint/router]
