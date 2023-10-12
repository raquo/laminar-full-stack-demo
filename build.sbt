import org.scalajs.linker.interface.ModuleSplitStyle

import scala.sys.process.Process

ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := Versions.Scala_3

// Run the frontend development loop (also run vite: `cd frontend; npm run dev`)
addCommandAlias("ffast", ";~frontend/fastLinkJS")
// Start the backend server, and make sure to stop it afterwards
addCommandAlias("sup", ";server/reStop ;~server/reStart ;server/reStop")
// Build frontend for production
addCommandAlias("fbuild", ";buildFrontend")
// Package the application into a jar. Run the jar with: `java -jar dist/app.jar`
addCommandAlias("jar", ";packageApplication")

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .in(file("./shared"))
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
    ),
    assembly / mainClass := Some("com.raquo.server.Server"),
    assembly / assemblyJarName := "app.jar",

    // #TODO once my backend dependencies stabilize, remove or reduce this block, since we don't use armeria anymore.
    // Get rid of "(server / assembly) deduplicate: different file contents found in the following" errors
    // https://stackoverflow.com/questions/54834125/sbt-assembly-deduplicate-module-info-class
    // #TODO Ask people if this is ok
    assembly / assemblyMergeStrategy := {
      case x if x.endsWith("module-info.class") => MergeStrategy.discard
      case x if x.endsWith("META-INF/io.netty.versions.properties") => MergeStrategy.discard
      case x if x.endsWith("META-INF/com.linecorp.armeria.versions.properties") => MergeStrategy.discard
      case x if x.endsWith("META-INF/native/lib/libnetty-unix-common.a") => MergeStrategy.discard
      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    }

  )
  .dependsOn(shared.jvm)

lazy val frontend = project
  .in(file("./frontend"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
  .settings(
    libraryDependencies ++= List(
      "com.raquo" %%% "laminar" % Versions.Laminar,
      "com.raquo" %%% "waypoint" % Versions.Waypoint,
    ),
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        // .withModuleSplitStyle(
        //   ModuleSplitStyle.SmallModulesFor(List("com.raquo.app")))
    },
    // Ignore changes to .less and .css files when watching files with sbt.
    // With the suggested build configuration and usage patterns, these files are
    // not included in the scala.js output, so there is no need for sbt to watch
    // their contents. If sbt was also watching those files, editing them would
    // cause the entire Scala.js app to do a full reload, whereas right now we
    // have Vite watching those files, and it is able to hot-reload them without
    // reloading the entire application – much faster and smoother.
    watchSources := watchSources.value.filterNot { source =>
      source.base.getName.endsWith(".less") || source.base.getName.endsWith(".css")
    },
    // Generated scala.js output will call your main() method to start your app.
    scalaJSUseMainModuleInitializer := true,
    // ScalablyTyped needs to know the directory with package.json
    externalNpm := baseDirectory.value
  )
  .dependsOn(shared.js)

val buildFrontend = taskKey[Unit]("Build frontend")

buildFrontend := {
  // Generate Scala.js JS output for production
  (frontend / Compile / fullLinkJS).value

  // Install JS dependencies from package-lock.json
  val npmCiExitCode = Process("npm ci", cwd = (frontend / baseDirectory).value).!
  if (npmCiExitCode > 0) {
    throw new IllegalStateException(s"npm ci failed. See above for reason")
  }

  // Build the frontend with vite
  val buildExitCode = Process("npm run build", cwd = (frontend / baseDirectory).value).!
  if (buildExitCode > 0) {
    throw new IllegalStateException(s"Building frontend failed. See above for reason")
  }

  // Copy vite output into server resources, where it can be accessed by the server,
  // even after the server is packaged in a fat jar.
  IO.copyDirectory(
    source = (frontend / baseDirectory).value / "dist",
    target = (server / baseDirectory).value / "src" / "main" / "resources" / "static"
  )
}

// Always build the frontend first before packaging the application in a fat jar
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
