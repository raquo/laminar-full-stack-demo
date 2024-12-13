package com.raquo.app.integrations

import com.raquo.airstream.web.AjaxStream.AjaxStreamError
import com.raquo.airstream.web.WebStorageVar
import com.raquo.app.JsRouter.*
import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.app.pages.*
import com.raquo.laminar.api.L.*
import com.raquo.utils.JsonUtils.*
import com.raquo.weather.Gradient
import io.bullet.borer.{Codec, Json}
import io.bullet.borer.derivation.MapBasedCodecs.*
import org.scalajs.dom

import java.nio.charset.StandardCharsets
import scala.scalajs.js
import scala.util.Success

object LocalStorageView {

  def apply(): HtmlElement = {
    div(
      h1("Local Storage & Session Storage"),
      p("The browser's ", a(href("https://developer.mozilla.org/en-US/docs/Web/API/Window/localStorage"), "localStorage"), " is a simple key-value storage that is shared across all browser tabs and frames of the same origin."),
      p(a(href("https://developer.mozilla.org/en-US/docs/Web/API/Window/sessionStorage"), "SessionStorage"), " is similar but with a different, more limited lifetime – it is scoped to a single browser tab (but still survives e.g. page reloads and navigation)."),
      p("To test LocalStorage syncing, ", a(href(dom.document.location.href), target("_blank"), "open this page in a new tab"), ", and look at both tabs side-by-side."),
      p(i("Note: this example will work on localhost, but not on a file:// url.")),
      LocalStorageTester(),
      CodeSnippets(_.`localstorage/localstorage-tester`, startExpanded = _ => false, caption = "LocalStorage example source:"),
      SessionStorageTester(),
      CodeSnippets(_.`localstorage/sessionstorage-tester`, caption = "SessionStorage example source:"),
    )
  }

  object LocalStorageTester {

    // BEGIN[localstorage/localstorage-tester]
    case class Person(id: Int, name: String)

    given personCodec: Codec[Person] = deriveCodec

    private val textVar = WebStorageVar
      .localStorage(
        key = "input-text",
        syncOwner = Some(unsafeWindowOwner)
      )
      .text(default = "")

    private val personVar = WebStorageVar
      .localStorage(
        key = "foo",
        syncOwner = Some(unsafeWindowOwner)
      )
      .withCodec[Person](
        encode = foo => Json.encode(foo).toUtf8String,
        decode = str => Json.decode(str.getBytes(StandardCharsets.UTF_8)).to[Person].valueTry,
        default = Success(Person(1, "Martin"))
      )

    private val idVar: Var[Int] = personVar.zoomLazy(_.id)((p, newId) => p.copy(id = newId))

    private val nameVar: Var[String] = personVar.zoomLazy(_.name)((p, newName) => p.copy(name = newName))

    // END[localstorage/localstorage-tester]

    def apply(): HtmlElement = {
      // BEGIN[localstorage/localstorage-tester]
      div(
        h2("LocalStorage", titleLink("localstorage")),
        p("If local storage is working, the values in these inputs should sync immediately between multiple tabs. Closing the tabs or even the browser completely, then navigating to the same URL afresh should also show retain the text."),
        availabilityChecker("LocalStorage", WebStorageVar.isLocalStorageAvailable),
        h3("Simple string"),
        p(
          label("Synced text: ", marginRight.px(8)),
          input(
            typ("text"),
            placeholder("Enter text here"),
            inputStyles,
            size(10),
            value <-- textVar,
            onInput.mapToValue --> textVar
          )
        ),
        h3("Json-encoded case class Person"),
        p(
          label("ID: ", marginRight.px(8)),
          input(
            typ("text"),
            placeholder("Enter numeric ID here"),
            size(5),
            maxLength(5),
            inputStyles,
            controlled(
              value <-- idVar.signal.map(_.toString),
              onInput.mapToValue.filter(_.forall(_.isDigit)).map(_.toInt) --> idVar
            )
          ),
        ),
        p(
          label("Name: ", marginRight.px(8)),
          input(
            typ("text"),
            placeholder("Enter name here"),
            inputStyles,
            size(10),
            value <-- nameVar.signal,
            onInput.mapToValue --> nameVar
          ),
        ),
        p(
          label("Synced value: ", marginRight.px(8)),
          span(
            display.inlineBlock,
            fontFamily("monospace"),
            inputStyles,
            readOnly(true),
            text <-- personVar.rawStorageValues.map(_.getOrElse("<None>"))
          )
        )
      )
      // END[localstorage/localstorage-tester]
    }
  }

  object SessionStorageTester {

    // BEGIN[localstorage/sessionstorage-tester]
    private val commentVar = WebStorageVar
      .sessionStorage(
        key = "comment",
        syncOwner = None // We don't have multiple frames in this page, so no point in syncing
      )
      .text(default = "")

    def apply(): HtmlElement = {
      div(
        display.flex,
        flexDirection.column,
        h2("SessionStorage", titleLink("sessionstorage")),
        p("If you go to any other URL in this tab, then come back using the browser's back button, reload the page, close the tab, then un-close / recover it (cmd+shift+T in most browsers) – the comment below should persist through all that, as that is all within one page session."),
        availabilityChecker("SessionStorage", WebStorageVar.isSessionStorageAvailable),
        h3("Comment saved in this page session"),
        textArea(
          inputStyles,
          placeholder("Write something here... a long PR description perhaps."),
          value <-- commentVar,
          onInput.mapToValue --> commentVar
        )
      )
    }
    // END[localstorage/sessionstorage-tester]
  }

  private def availabilityChecker(label: String, check: () => Boolean): HtmlElement = {
    val storageAvailableVar = Var(check())
    p(
      span(
        text <-- storageAvailableVar.signal.map {
          case true => s"✅ $label is available"
          case false => s"🛑 User denied access to $label"
        },
        marginRight.px(10),
      ),
      button(
        typ("button"),
        inContext { thisNode =>
          text <--
            thisNode.events(onClick)
              .delayWithStatus(300)
              .map {
                case Pending(_) => "Checking..."
                case _ => "Check again"
              }
              .startWith("Check again")
        },
        onClick.mapTo(WebStorageVar.isLocalStorageAvailable()) --> storageAvailableVar,
      )
    )
  }

  private val inputStyles = Seq(
    padding.px(10),
    fontSize.large
  )
}
