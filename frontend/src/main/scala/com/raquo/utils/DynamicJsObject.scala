package com.raquo.utils

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

/**
  * These updateDynamic helpers provide an easy way to set rarely
  * used values on JS types that you don't care to type properly.
  *
  * For example, we use one of them to set a custom backgroundColor
  * on ChartDataset. We have alternatives to that:
  *
  * 1) We could add a backgroundColor property to the constructor of
  *    ChartDataset, then we can set it like any other parameter.
  *
  * 2) We could create an instance of an anonymous class extending
  *    ChartDataset using the `new` keyword, and define the
  *    background property just for that class, like so:
  *
  *    new ChartDataset(..constructor params..) {
  *      val backgroundColor: String = ...
  *    }
  */
class DynamicJsObject extends js.Object {

  // Note: js.Object classes have significant caveats when it comes to
  // overloading methods, so it's best to define unique JSName-s for them
  // to avoid runtime dispatch and surprising behaviour.
  // See https://www.scala-js.org/doc/interoperability/sjs-defined-js-classes.html

  // Note: overloading these methods causes problems with implicit resolution,
  // for example updateDynamic("foo" -> 1, "bar" -> "yes") fails to compile with
  // "None of the overloaded alternatives of method updateDynamic in class ...
  // match arguments", even though commenting out one of them works. I think is
  // because the compiler fails to consider the implicit conversions from `1` and
  // `"yes"` to js.Any in case of overloads. I'm not sure if it is supposed to, but
  // it would have been nice if that worked. #TODO[Scala]

  //def updateDynamic(key: String, value: js.Any): this.type = {
  //  this.asInstanceOf[js.Dynamic].updateDynamic(key)(value)
  //  this
  //}

  //@JSName("updateDynamicPair")
  //def updateDynamic(keyValuePair: (String, js.Any)): this.type = {
  //  this.asInstanceOf[js.Dynamic].updateDynamic(keyValuePair._1)(keyValuePair._2)
  //  this
  //}

  //@JSName("updateDynamicPairs")
  def updateDynamic(keyValuePairs: (String, js.Any)*): this.type = {
    keyValuePairs.foreach { (key, value) =>
      this.asInstanceOf[js.Dynamic].updateDynamic(key)(value)
    }
    this
  }
}
