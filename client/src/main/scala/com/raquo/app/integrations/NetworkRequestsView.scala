package com.raquo.app.integrations

import com.raquo.airstream.web.AjaxStream.AjaxStreamError
import com.raquo.app.JsRouter.*
import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.laminar.api.L.*
import com.raquo.laminar.api.features.unitArrows
import com.raquo.weather.Gradient
import org.scalajs.dom

import scala.scalajs.js

object NetworkRequestsView {

  def apply(): HtmlElement = {
    div(
      h1("Network Requests"),
      p("You don't need to use any third party libraries to make network requests. Airstream provides simple interfaces to the browser's Fetch and Ajax APIs."),
      ul(
        li(a(href("https://laminar.dev/documentation#network-requests"), "Laminar docs on network requests")),
        li(a(href("https://github.com/raquo/Airstream/#fetchstream"), "Airstream docs on FetchStream")),
        li(a(href("https://github.com/raquo/Airstream/#ajax"), "Airstream docs on AjaxStream"))
      ),
      p("For a more advanced networking example including shared models, JSON codecs, and a backend, see the ", a(navigateTo(WeatherGradientPage(Gradient.Squamish.id)), "Weather gradient app"), "."),
      // BEGIN[network/fetch-tester]
      FetchTester(),
      // END[network/fetch-tester]
      CodeSnippets(_.`network/fetch-tester`),
      // BEGIN[network/ajax-tester]
      AjaxTester.app,
      // END[network/ajax-tester]
      CodeSnippets(_.`network/ajax-tester`),
    )
  }

  // BEGIN[network/fetch-tester]
  object FetchTester {

    // Example based on plain JS version: http://plnkr.co/edit/ycQbBr0vr7ceUP2p6PHy?preview

    case class FetchOption(name: String, baseUrl: String, bustCache: Boolean = false) {
      def id: String = "fetch-" + name
      def url: String = if (bustCache) baseUrl + "?t=" + js.Date.now() else baseUrl
    }

    private val options = List(
      FetchOption("Valid Fetch request", "https://api.zippopotam.us/us/90210"),
      FetchOption("Download 10MB file (gives you time to abort)", "https://cachefly.cachefly.net/10mb.test", bustCache = true),
      FetchOption("Download 100MB file (gives you time to abort)", "https://cachefly.cachefly.net/100mb.test", bustCache = true),
      FetchOption("URL that will fail due to invalid domain", "https://api.zippopotam.uxx/us/90210"),
      FetchOption("URL that will fail due to CORS restriction", "https://unsplash.com/photos/KDYcgCEoFcY/download?force=true")
    )

    def apply(): HtmlElement = {
      val selectedOptionVar = Var(options.head)
      val eventsVar = Var(List.empty[String])
      val (abortStream, abort) = EventStream.withUnitCallback

      form(
        h2("Fetch API tester", titleLink("fetch-tester")),
        options.map { option =>
          div(
            input(
              idAttr(option.id),
              typ("radio"),
              nameAttr("fetchOption"),
              checked <-- selectedOptionVar.signal.map(_ == option),
              onChange.mapTo(option) --> selectedOptionVar,
            ),
            label(forId(option.id), " " + option.name)
          )
        },
        br(),
        div(
          button(
            typ("button"),
            "Send",
            inContext { thisNode =>
              val clicks = thisNode.events(onClick).sample(selectedOptionVar.signal)
              val responses = clicks.flatMapSwitch { opt =>
                FetchStream.get(url = opt.url, _.abortStream(abortStream))
                  .map(resp => if (resp.length >= 1000) resp.substring(0, 1000) else resp)
                  .map("Response (first 1000 chars): " + _)
                  .recover { case err: Throwable => Some(err.getMessage) }
              }

              List(
                clicks.map(opt => List(s"Starting: GET ${opt.url}")) --> eventsVar,
                responses --> eventsVar.updater[String](_ :+ _)
              )
            }
          ),
          " ",
          button(
            typ("button"),
            "Abort",
            // Note: using advanced Laminar syntax feature – see https://laminar.dev/documentation#-unit-sinks
            onClick --> abort()
            // onClick.mapTo(()) --> abort // Alternative syntax
          )
        ),
        div(
          fontSize.em(0.8),
          br(),
          b("Events:"),
          div(children <-- eventsVar.signal.map(_.map(div(_))))
        )
      )
    }
  }
  // END[network/fetch-tester]

  // BEGIN[network/ajax-tester]
  object AjaxTester {

    // Example based on plain JS version: http://plnkr.co/edit/ycQbBr0vr7ceUP2p6PHy?preview

    case class AjaxOption(name: String, baseUrl: String, bustCache: Boolean = false) {
      def id: String = "ajax-" + name
      def url: String = if (bustCache) baseUrl + "?t=" + js.Date.now() else baseUrl
    }

    private val options = List(
      AjaxOption("Valid Ajax request", "https://api.zippopotam.us/us/90210"),
      AjaxOption("Download 10MB file (gives you time to abort)", "https://cachefly.cachefly.net/10mb.test", bustCache = true),
      AjaxOption("Download 100MB file (gives you time to abort)", "https://cachefly.cachefly.net/100mb.test", bustCache = true),
      AjaxOption("URL that will fail due to invalid domain", "https://api.zippopotam.uxx/us/90210"),
      AjaxOption("URL that will fail due to CORS restriction", "https://unsplash.com/photos/KDYcgCEoFcY/download?force=true")
    )
    private val selectedOptionVar = Var(options.head)
    private val pendingRequestVar = Var[Option[dom.XMLHttpRequest]](None)
    private val eventsVar = Var(List.empty[String])

    val app: HtmlElement = form(
      h2("Ajax API tester", titleLink("ajax-tester")),
      options.map { option =>
        div(
          input(
            typ("radio"),
            idAttr(option.id),
            nameAttr("ajaxOption"),
            checked <-- selectedOptionVar.signal.map(_ == option),
            onChange.mapTo(option) --> selectedOptionVar
          ),
          label(forId(option.id), " " + option.name)
        )
      },
      br(),
      div(
        button(
          typ("button"),
          "Send",
          inContext { thisNode =>
            val clickStream = thisNode.events(onClick).sample(selectedOptionVar.signal)
            val responseStream = clickStream.flatMapSwitch { opt =>
              AjaxStream
                .get(
                  url = opt.url,
                  // These observers are optional, we're just using them for demo
                  requestObserver = pendingRequestVar.someWriter,
                  progressObserver = eventsVar.updater { (evs, p) =>
                    val ev = p._2
                    evs :+ s"Progress: ${ev.loaded} / ${ev.total} (lengthComputable = ${ev.lengthComputable})"
                  },
                  readyStateChangeObserver = eventsVar.updater { (evs, req) =>
                    evs :+ s"Ready state: ${req.readyState}"
                  }
                )
                .map("Response: " + _.responseText)
                .recover { case err: AjaxStreamError => Some(err.getMessage) }
            }

            List(
              clickStream.map(opt => List(s"Starting: GET ${opt.url}")) --> eventsVar,
              responseStream --> eventsVar.updater[String](_ :+ _)
            )
          }
        ),
        " ",
        button(
          typ("button"),
          "Abort",
          onClick --> (_ => pendingRequestVar.now().foreach(_.abort()))
        )
      ),
      div(
        fontSize.em(0.8),
        br(),
        b("Events:"),
        div(children <-- eventsVar.signal.map(_.map(div(_))))
      )
    )
  }
  // END[network/ajax-tester]
}
