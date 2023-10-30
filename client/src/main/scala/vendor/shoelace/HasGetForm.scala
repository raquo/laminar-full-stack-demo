package vendor.shoelace

import org.scalajs.dom

import scala.scalajs.js

@js.native
trait HasGetForm extends js.Object {

  /** Gets the associated form, if one exists. */
  def getForm(): dom.HTMLFormElement | Null
}
