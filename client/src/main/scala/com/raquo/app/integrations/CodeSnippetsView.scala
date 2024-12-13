package com.raquo.app.integrations

import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.laminar.api.L.{*, given}

object CodeSnippetsView {

  def apply(): HtmlElement = {
    div(
      h1("Code Snippets"),
      p("Across this demo app you see code snippets like this:"),
      // BEGIN[codesnippets/usage]
      CodeSnippets(_.`hello world`, asParagraph = true),
      // END[codesnippets/usage]
      p("They show the actual pieces of the relevant code, extracted from the codebase at compile time. So, we're getting compile-time data from sbt all the way into Scala.js. For simple data structures, this can be done with the ", a(href("https://github.com/sbt/sbt-buildinfo"), "sbt-buildinfo"), " sbt plugin, and we do in fact use it to display the Laminar version number on the home page, but the way we process code snippets gets us static type safety which is not possible with sbt-buildinfo. So, here is how we do this."),
      p("First, we setup a precompile task in sbt, and make the compile task depend on it. So, whenever the `client` project is compiled, we run the script that finds and generates the snippets."),
      CodeSnippets(_.`codesnippets/precompile`, asParagraph = true),
      p("In turn, our CodeSnippetsGenerator calls into CodeBrowser.findCodeSnippets, which recursively walks the file tree, and finds matching ", code("// BEGIN[hello world]"), " and ", code("// END[hello world]"), " comments, grabbing the code snippet in between, removing extra whitespace, etc. I'll spare you all the java.io.File glory, the important part is that we get a list of snippets for every key like ", code("hello world"), "."),
      CodeSnippets(_.`codesnippets/generator`, asParagraph = true),
      p("As you see we just generate Scala code line by line. It's very easy. I'm using the SourceGenerator trait from ", a(href("https://github.com/raquo/scala-dom-types"), "Scala DOM Types"), ", which I've added as a compile-time dependency in ", code(b("project/"), "build.sbt"), ":"),
      CodeSnippets(_.`compile-time-build.sbt`, asParagraph = true),
      p("So what do we get when everything is generated? A regular looking ", a(href("https://github.com/raquo/laminar-full-stack-demo/blob/master/client/src/main/scala/com/raquo/app/codesnippets/generated/GeneratedSnippets.scala"), "GeneratedSnippets.scala"), " file in the client project, with an object that has many val-s, one for each key, containing a list of snippets for that key."),
      p("Finally, we can render those snippets on the frontend like so:"),
      CodeSnippets(_.`codesnippets/usage`, asParagraph = true),
      p("The implementation is pretty straightforward, and uses the ", a(href("https://highlightjs.org/"), "Highlight.js"), " library for syntax highlighting."),
      CodeSnippets(_.`codesnippets/object`.sortBy(_.fileName != "CodeSnippets.scala"), asParagraph = true, startExpanded = _.fileName == "CodeSnippets.scala"),
      p("Finally, here are the interfaces I made for the Highlight.js library. The interfaces themselves take about 30 lines, and integration with Laminar is another 10 lines."),
      CodeSnippets(_.`codesnippets/highlight.js`.sortBy(_.fileName != "hljs.scala"), asParagraph = true)
    )
  }
}
