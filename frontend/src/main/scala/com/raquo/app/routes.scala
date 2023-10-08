package com.raquo.app

import com.raquo.app.pages.*
import com.raquo.waypoint.*

// -- Routes --

// Note: For every URL covered by these routes, the backend should
// serve the index.html file that loads your application.
// In Server.scala, we do this for `/` and for every URL under `/app`.

val appRoot = root / "app"

val routes = List(
  Route.static(HomePage, root / endOfSegments),
  Route[WeatherGradientPage, String](
    encode = page => page.gradientId,
    decode = gradientId => WeatherGradientPage(gradientId = gradientId),
    pattern = appRoot / "weather" / "gradient" / segment[String] / endOfSegments
  )
)
