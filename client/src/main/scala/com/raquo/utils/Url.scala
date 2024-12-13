package com.raquo.utils

import scala.scalajs.js

/**
  * Dead simple helper to construct URLs with automatic URL-encoding of segments and params.
  *
  * You're free to not use it, Airstream's Ajax and Fetch streams expect plain strings for URLs.
  *
  * Example URLs:
  *
  *  - relRoot / "foo" / "bar" ? ("query" -> "pan cakes") & ("quantity" -> "yes")
  *    `foo/bar?query=pan%20cakes&quantity=yes`
  *  - the same but with `absRoot` instead of `relRoot` would start with `/foo` instead of `foo`
  *  - using `hostRoot("http://example.com")` instead would yield `http://example.com/foo...`
  *
  * This class doesn't do any URL validation, but it does encode the segments, query param names,
  * and query param values using native JS encodeURIComponent method.
  *
  * Using `encodeURIComponent` is very important. Without it, data can corrupt the URL structure,
  * for example `absRoot / "users" / userId` could become `/users/123/foo?bar=yes` if the variable
  * `userId` contains the string "123/foo?bar=yes". But with our precautions here, we will get
  * `/users/123%2Ffoo%3Fbar%3Dyes` instead, which is what one would expect. Note that good backend
  * frameworks decode such URI encoding automatically, so it should be transparent to your code.
  */
class Url(str: String) {

  /** Add trailing slash */
  def `/`: Url = / ("")

  /** Add path segment */
  def /(segment: String): Url = {
    val prefix = if (str == "" || (str.endsWith("/") & !str.endsWith("//"))) str else str + "/"
    Url(prefix + js.URIUtils.encodeURIComponent(segment))
  }

  /** Start query params with a key-value pair */
  def ?(paramKeyValue: (String, String)): Url = {
    Url(str + "?" + Url.encodeQueryParam(paramKeyValue._1, paramKeyValue._2))
  }

  /** Add another key-value pair to query params */
  def &(paramKeyValue: (String, String)): Url = {
    Url(str + "&" + Url.encodeQueryParam(paramKeyValue._1, paramKeyValue._2))
  }

  override def toString: String = str
}

object Url {

  val relRoot: Url = Url("")

  val absRoot: Url = Url("/")

  /** Don't include the trailing slash */
  def hostRoot(host: String): Url = Url(host)

  def encodeQueryParam(key: String, value: String): String = {
    val k = js.URIUtils.encodeURIComponent(key)
    val v = js.URIUtils.encodeURIComponent(value)
    k + "=" + v
  }

  extension (url: Url)
    def ?(keyValue: (String, Seq[String])): Url = {
      val key = keyValue._1
      val values = keyValue._2
      Url(url.toString + "&" + values.map(value => Url.encodeQueryParam(key, value).mkString("&")))
    }

  given urlToString: Conversion[Url, String] with
    def apply(url: Url): String = url.toString
}

// #TODO think about it and remove
// Some half-based URL-DSL based alternatives:
//
// (root / "api" / "weather" / "gradient" / segment[String]) ? param[String]("version") << (gradient.id, "1")
//
//given patternToUrlString: Conversion[UrlPart[Unit, _], String] with
//  def apply(path: UrlPart[Unit, _]): String = "/" + path.createPart()
//
//extension [A](pattern: UrlPart[A, _])
//
//  def <<(values: A): String = {
//    "/" + pattern.createPart(values)
//  }
//
//extension [P, Q](pattern: UrlPart[UrlMatching[P, Q], _])
//
//  def <<[V](values: V)(implicit c: Composition.Aux[P, Q, V]): String = {
//    val (pathValues, queryValues) = c.decompose(values)
//    "/" + pattern.createPart(UrlMatching(pathValues, queryValues))
//  }
//
