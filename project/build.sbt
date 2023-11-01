// BEGIN[compile-time-build.sbt]
// #Note this is /project/build.sbt, it is used to build the build
//  Back out of this inception, see /build.sbt for the main build config.

lazy val root = (project in file("."))
  .settings(
    // Compile-time dependencies
    // - Unlike regular dependencies, these are available in build.sbt
    // - We use a source generator from Scala DOM Types
    //   to generate code snippets for the frontend at compile time.
    libraryDependencies ++= Seq(
      "com.raquo" %% "domtypes" % "17.1.0"
    )
  )
// END[compile-time-build.sbt]
