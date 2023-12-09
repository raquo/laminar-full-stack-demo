package com.raquo.utils

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom

import scala.scalajs.js

object Utils {

  /** Marks the otherwise-unused import as "used" in Scala.js,
    * preventing dead code elimination.
    */
  def useImport(importedObject: js.Any): Unit = ()

  extension [A](a: A)
    def some: Some[A] = Some(a)

}
