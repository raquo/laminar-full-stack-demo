package vendor.shoelace

import com.raquo.airstream.core.Transaction
import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import com.raquo.laminar.tags.HtmlTag
import com.raquo.utils.Utils.useImport
import org.scalajs.dom

import scala.scalajs.js

// BEGIN[shoelace/components]
/** Base trait for all web components. */
trait WebComponent(tagName: String) extends CommonTypes {

  type Ref <: dom.HTMLElement

  type El = ReactiveHtmlElement[Ref]

  type ModFunction = this.type => Mod[El]

  type ComponentMod = ModFunction | Mod[El]

  protected val RawImport: js.Any

  useImport(RawImport)

  protected def tag: HtmlTag[Ref] = htmlTag(tagName)
  
  /** Instantiate this component using the specified modifiers.
    *
    * Modifiers can be the usual Laminar modifiers, or they can be functions from this component to a modifier. Allowing
    * these functions is very practical to access the reactive attributes of the component, with the `_.reactiveAttr`
    * syntax.
    *
    * Scala 2, use the `of` method. See https://github.com/sherpal/LaminarSAPUI5Bindings#remark-for-scala-213-users
    * IntelliJ users, consider using the `of` method. See https://youtrack.jetbrains.com/issue/SCL-21713/Method-accepting-a-union-of-types-that-includes-a-Function-type-problems-with-go-to-definition-type-hints-and-autocomplete-Scala
    */
  final def apply(mods: ComponentMod*): El = {
    val el = tag()
    Transaction.onStart.shared {
      mods.foreach {
        case mod: Mod[_ >: El]                        => mod(el)
        case modFn: Function[_ >: this.type, _ <: Mod[El]] => modFn(this)(el)
      }
    }
    el
  }

  /** Same as [[apply]], but accept only [[ModFunction]]s */
  final def of(mods: ModFunction*): El = {
    val el = tag()
    Transaction.onStart.shared {
      mods.foreach(_(this)(el))
    }
    el
  }

}
// END[shoelace/components]
