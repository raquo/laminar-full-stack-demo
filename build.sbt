import scala.sys.process.Process

ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "3.2.0"

val circeVersion = "0.14.1"

lazy val `shared-logic` = crossProject(JSPlatform, JVMPlatform)
  .in(file("./shared-logic"))
  .settings(
    // circe for jvm-js communication
    libraryDependencies ++= List(
      "io.circe" %%% "circe-core",
      "io.circe" %%% "circe-generic",
      "io.circe" %%% "circe-parser"
    ).map(_ % circeVersion)
  )

lazy val server = project
  .in(file("./server"))
  .settings(
    libraryDependencies ++= List(
      // cask as server (other choices are zio-http, http4s, akka-http, play...)
      "com.lihaoyi" %% "cask" % "0.8.3"
    ),
    assembly / mainClass := Some("server.Server"),
    assembly / assemblyJarName := "app.jar"
  )
  .dependsOn(`shared-logic`.jvm)

def esModule = Def.settings(scalaJSLinkerConfig ~= {
  _.withModuleKind(ModuleKind.ESModule)
})

lazy val frontend = project
  .in(file("./frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= List(
      // web framework (other choices are slinky, scala-js-react, outwatch...)
      "com.raquo" %%% "laminar" % "0.14.5",
      // web component library (other (non-exclusive) choices are material-ui, bootstrap...)
      "be.doeraene" %%% "web-components-ui5" % "1.8.0"
    ),
    esModule,
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(`shared-logic`.js)

val buildFrontend = taskKey[Unit]("Build frontend")

buildFrontend := {
  /*
  To build the frontend, we do the following things:
  - fullLinkJS the frontend sub-module
  - run npm ci in the frontend directory (might not be required)
  - package the application with vite-js (output will be in the resources of the server sub-module)
   */
  (frontend / Compile / fullLinkJS).value
  val npmCiExit = Process(Utils.npm :: "ci" :: Nil, cwd = baseDirectory.value / "frontend").run().exitValue()
  if (npmCiExit > 0) {
    throw new IllegalStateException(s"npm ci failed. See above for reason")
  }

  val buildExit = Process(Utils.npm :: "run" :: "build" :: Nil, cwd = baseDirectory.value / "frontend").run().exitValue()
  if (buildExit > 0) {
    throw new IllegalStateException(s"Building frontend failed. See above for reason")
  }

  IO.copyDirectory(baseDirectory.value / "frontend" / "dist", baseDirectory.value / "server" / "src" / "main" / "resources" / "static")
}

(server / assembly) := (server / assembly).dependsOn(buildFrontend).value

val packageApplication = taskKey[File]("Package the whole application into a fat jar")

packageApplication := {
  /*
  To package the whole application into a fat jar, we do the following things:
  - call sbt assembly to make the fat jar for us (config in the server sub-module settings)
  - we move it to the ./dist folder so that the Dockerfile can be independent of Scala versions and other details
   */
  val fatJar = (server / assembly).value
  val target = baseDirectory.value / "dist" / "app.jar"
  IO.copyFile(fatJar, target)
  target
}
