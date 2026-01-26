package com.raquo.app.features

import com.raquo.laminar.api.L.{*, given}

// BEGIN[code-splitting]
// This object is lazy-loaded from CodeSplittingView. For the sake of demoing dynamic imports, don't use it elsewhere.
object LazyLoadedView {

  def apply(triggerCount: Int): HtmlElement =
    div(
      borderRadius.px(8),
      background("#fffff0"),
      padding.px(20),
      h2("This section was just lazy-loaded!"),
      p("Any modules that are only used within this section will have been downloaded just now – you can check the Network tab of your browser dev tools: see the last downloaded JS file, and find this unique string in its content to quickly locate the source code of this module: SQFHAZRYU."),
      p("Note that code splitting happens on the module level. This is why this section lives in its own `LazyLoadedView`. That module is not used anywhere else in this app, so it was only loaded the first time it was needed, when you clicked the button."),
      p("In contrast, consider that this section uses Laminar's ", code("when"), " method, and yet the code for this method wasn't lazy-loaded, it was loaded upfront. That's because this method is defined in a module (the main Laminar / L object) that is used method in (many) parts of the demo app that aren't lazy-loaded, so we need that module, and thus that method, upfront."),
      p("You need to carefully organize your code to get the benefits of lazy loading."),
      when(triggerCount > 1)(
        p(s"You clicked the button ", b(triggerCount), " times, but the section was lazy-loaded only once – on the first click. Subsequent clicks don't cause re-downloading, they only cause re-running of this code inside the `dynamicImport` argument.")
      )
    )
}
// END[code-splitting]
