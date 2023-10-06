package com.raquo.utils

import scala.scalajs.js

object JsUndefinedSyntax {

  // - I don't like writing js.UndefOr[A], I would prefer A | jsUndefined
  // - But A | jsUndefined is not the same type as js.UndefOr[A]
  // - They have the same runtime representation, but different compile time type
  // - I'm a bit worried that some libraries (e.g. JSON codecs) can't yet handle
  //   the union type properly, or handle it in an overly complicated manner,
  //   without the knowledge of this special case
  // - So here's another helper A || jsUndefined – this desugars to js.UndefOr[A],
  //   but looks nicer.
  // - I haven't used these much.

  //   # TODO
  // - I've since realized that you can say `A | Unit`, and it seems to work
  //   well in my limited testing. Unit is js.undefined at runtime, so, only a question of types.
  //   Scala.js has a bunch of necessary implicit conversions for this, let's
  //   hope they work well.

  /** Only js.undefined is of this type */
  type jsUndefined = js.UndefOr[Nothing] // type of js.undefined

  /**  */
  type ||[A, U <: jsUndefined] = js.UndefOr[A]

  //object ignoreTest1 {
  //  var x: js.UndefOr[Int] = ???
  //  var y: Int || jsUndefined = ???
  //  var z: Int | jsUndefined = ???
  //  x = y
  //  y = x
  //  x = z
  //  z = x // this uses `fromTypeConstructor` noop implicit conversion
  //  y = z
  //  z = y
  //}

}

