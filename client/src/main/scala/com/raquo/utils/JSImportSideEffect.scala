package com.raquo.utils

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

object JSImportSideEffect {

  /** Call this function to import a JS / CSS file just for the
    * sake of its side effects.
    *
    * For this to work, you must pass the custom JSGlobal name
    * as `importFnName` option to `importSideEffectPlugin` in
    * vite.config.js, and that name must be unique in your codebase.
    *
    * When used together with globResolverPlugin, you can say this:
    *
    *   JsImportSideEffect("@find/**/foo.css")
    *
    * and Vite will find the foo.css file in your codebase,
    * and will insert the correct import for it:
    *
    *   import "/path/to/foo.css"
    *
    * See vite.config.js and the files in the vite-plugins directory.
    */
  @js.native
  @JSGlobal("importSideEffect_3DfPjKW0ZYyY")
  def apply(moduleId: String): Unit = js.native
}
