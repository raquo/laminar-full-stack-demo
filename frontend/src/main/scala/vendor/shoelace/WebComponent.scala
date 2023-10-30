package vendor.shoelace

import com.raquo.airstream.core.Transaction
import com.raquo.laminar.api.L.*
import com.raquo.laminar.codecs.*
import com.raquo.laminar.keys
import com.raquo.laminar.modifiers.KeySetter
import org.scalajs.dom
import com.raquo.laminar.nodes.ReactiveHtmlElement
import com.raquo.laminar.tags.HtmlTag
import com.raquo.utils.JSImportSideEffect

/** Marker "trait" that all web components inherit.
  *
  * This can allow you to implement some shenanigans and abstract over some things.
  */
abstract class WebComponent(tagName: String) extends CommonTypes {

  type Ref <: dom.HTMLElement

  type ModFunction = this.type => Mod[ReactiveHtmlElement[Ref]]

  type ComponentMod = ModFunction | Mod[ReactiveHtmlElement[Ref]]

  protected def tag: HtmlTag[Ref] = htmlTag(tagName)
  
  /** Instantiate this component using the specified modifiers.
    *
    * Modifiers can be the usual Laminar modifiers, or they can be functions from this component to a modifier. Allowing
    * these functions is very practical to access the reactive attributes of the component, with the `_.reactiveAttr`
    * syntax.
    */
  final def apply(mods: ComponentMod*): HtmlElement = {
    val el = tag()
    Transaction.onStart.shared {
      mods.foreach {
        case mod: Mod[_ >: ReactiveHtmlElement[Ref]] =>
          mod(el)
        case modFn: Function[_ >: this.type, _ <: ReactiveHtmlElement[Ref]] =>
          modFn(this)(el)
      }
    }
    el
  }

  /** Same as [[apply]], but accept only [[ModFunction]]s.
    *
    * This function is only there for people using the library with Scala 2.13.
    */
  final def of(mods: ModFunction*): HtmlElement = {
    val el = tag()
    Transaction.onStart.shared {
      mods.foreach(_(this)(el))
    }
    el
  }

}
