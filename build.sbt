import org.scalajs.linker.interface.ModuleSplitStyle

import scala.sys.process.Process

ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := Versions.Scala_3

lazy val root = project.in(file("."))
  .aggregate(client, server)
  .settings(
    name := "Laminar Demo"
  )
  .settings(noPublish)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .in(file("./shared"))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings)
  .settings(
    // sbt-BuildInfo plugin can write any (simple) data available in sbt at
    // compile time to a `case class BuildInfo` that it makes available at runtime.
    buildInfoKeys := Seq[BuildInfoKey](scalaVersion, sbtVersion, BuildInfoKey("laminarVersion" -> Versions.Laminar)),
    // The BuildInfo case class is located in target/scala<version>/src_managed,
    // and with this setting, you'll need to `import com.raquo.buildinfo.BuildInfo`
    // to use it.
    buildInfoPackage := "com.raquo.buildinfo"
    // Because we add BuildInfo to the `shared` project, this will be available
    // on both the client and the server, but you can also make it e.g. server-only.
  )
  .settings(
    libraryDependencies ++= List(
      // JSON codec
      "io.bullet" %%% "borer-core" % Versions.Borer,
      "io.bullet" %%% "borer-derivation" % Versions.Borer,
    )
  )
  .jvmSettings(
    libraryDependencies ++= List(
      // This dependency lets us put @JSExportAll and similar Scala.js
      // annotations on data structures shared between JS and JVM.
      // With this library, on the JVM, these annotations compile to
      // no-op, which is exactly what we need.
      "org.scala-js" %% "scalajs-stubs" % Versions.ScalaJsStubs
    )
  )

lazy val server = project
  .in(file("./server"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= List(
      // Effect library providing the IO type, used as a better alternative to scala.Future
      "org.typelevel" %% "cats-effect" % Versions.CatsEffect,
      // Http4s web server framework
      "org.http4s" %% "http4s-ember-server" % Versions.Http4s,
      "org.http4s" %% "http4s-dsl" % Versions.Http4s,
      // Logging
      "org.typelevel" %% "log4cats-slf4j" % Versions.Log4Cats,
      "ch.qos.logback" % "logback-classic" % Versions.Logback,
      // Http4s HTTP client to fetch data from the weather API
      "org.http4s" %% "http4s-ember-client" % Versions.Http4s,
      // XML decoder (to parse weather API XMLs)
      "ru.tinkoff" %% "phobos-core" % Versions.Phobos,
    )
  )
  .settings(
    assembly / mainClass := Some("com.raquo.server.Server"),
    assembly / assemblyJarName := "app.jar",

    // Gets rid of "(server / assembly) deduplicate: different file contents found in the following" errors
    // https://stackoverflow.com/questions/54834125/sbt-assembly-deduplicate-module-info-class
    assembly / assemblyMergeStrategy := {
      case path if path.endsWith("module-info.class") => MergeStrategy.discard
      case path =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(path)
    }
  )
  .dependsOn(shared.jvm)

lazy val client = project
  .in(file("./client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= List(
      "com.raquo" %%% "laminar" % Versions.Laminar,
      "com.raquo" %%% "waypoint" % Versions.Waypoint,
      "be.doeraene" %%% "web-components-ui5" % Versions.UI5
    ),
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        // .withModuleSplitStyle(
        //   ModuleSplitStyle.SmallModulesFor(List("com.raquo.app")))
    },
    // Generated scala.js output will call your main() method to start your app.
    scalaJSUseMainModuleInitializer := true
  )
  // BEGIN[codesnippets/precompile]
  .settings(
    precompile := {
      CodeSnippetsGenerator.generate(
        rootPath = java.nio.file.Path.of("."),
        targetPath = java.nio.file.Path.of("client/src/main/scala/com/raquo/app/codesnippets/generated"),
        packageName = "com.raquo.app.codesnippets.generated",
        objectName = "GeneratedSnippets"
      )
    },
    (Compile / compile) := ((Compile / compile) dependsOn precompile).value
  )
  // END[codesnippets/precompile]
  .settings(
    // Ignore changes to .less and .css files when watching files with sbt.
    // With the suggested build configuration and usage patterns, these files are
    // not included in the scala.js output, so there is no need for sbt to watch
    // their contents. If sbt was also watching those files, editing them would
    // cause the entire Scala.js app to do a full reload, whereas right now we
    // have Vite watching those files, and it is able to hot-reload them without
    // reloading the entire application – much faster and smoother.
    watchSources := watchSources.value.filterNot { source =>
      source.base.getName.endsWith(".less") || source.base.getName.endsWith(".css")
    }
  )
  .dependsOn(shared.js)

// BEGIN[codesnippets/precompile]
lazy val precompile = taskKey[Unit]("runs our own pre-compile tasks")
// END[codesnippets/precompile]

val buildClient = taskKey[Unit]("Build client (frontend)")

buildClient := {
  // Generate Scala.js JS output for production
  (client / Compile / fullLinkJS).value

  // Install JS dependencies from package-lock.json
  val npmCiExitCode = Process("npm ci", cwd = (client / baseDirectory).value).!
  if (npmCiExitCode > 0) {
    throw new IllegalStateException(s"npm ci failed. See above for reason")
  }

  // Build the frontend with vite
  val buildExitCode = Process("npm run build", cwd = (client / baseDirectory).value).!
  if (buildExitCode > 0) {
    throw new IllegalStateException(s"Building frontend failed. See above for reason")
  }

  // Copy vite output into server resources, where it can be accessed by the server,
  // even after the server is packaged in a fat jar.
  IO.copyDirectory(
    source = (client / baseDirectory).value / "dist",
    target = (server / baseDirectory).value / "src" / "main" / "resources" / "static"
  )
}

// Always build the frontend first before packaging the application in a fat jar
(server / assembly) := (server / assembly).dependsOn(buildClient).value

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

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation"
  )
)

lazy val noPublish = Seq(
  publishLocal / skip := true,
  publish / skip := true
)

// -- Aliases

// Run the frontend development loop (also run vite: `cd frontend; npm run dev`)
addCommandAlias("cup", ";~client/fastLinkJS")
// Start the backend server, and make sure to stop it afterwards
addCommandAlias("sup", ";server/reStop ;~server/reStart ;server/reStop")
// Build frontend for production
addCommandAlias("cbuild", ";buildClient")
// Package the application into a jar. Run the jar with: `java -jar dist/app.jar`
addCommandAlias("jar", ";packageApplication")
