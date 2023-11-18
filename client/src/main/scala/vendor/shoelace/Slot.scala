package vendor.shoelace

import com.raquo.laminar.api.L.*

// BEGIN[shoelace/components]
/** A [[Slot]] represents a special child component of web components.
  *
  * Many web components reserve a `slot` attribute for some of their children, with a particular meaning.
  *
  * In order to have compile-time fixed slots for your elements, you can define a variable with their name, and it will
  * allow you to attach child in a simple manner.
  */
final class Slot(name: String) {

  def :=(element: HtmlElement): HtmlElement = element.amend(slot := name)

  @inline def apply(element: HtmlElement): HtmlElement = (this := element)

  def <--(elementS: Source[HtmlElement]): Inserter.Base =
    child <-- elementS.toObservable.map(_.amend(slot := name))

}
// END[shoelace/components]
