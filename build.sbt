import org.scalajs.linker.interface.ModuleSplitStyle

// addDependencyTreePlugin

import scala.sys.process.Process

ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := Versions.Scala_3

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .in(file("./shared"))
  .settings(
    libraryDependencies ++= List(
      "com.github.plokhotnyuk.jsoniter-scala" %%% "jsoniter-scala-core" % Versions.JsoniterScala,
      // #TODO[Build] Using "provided" for macros instead of "compiler-internal" because IntelliJ does not understand the latter. Not sure if there's any difference.
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % Versions.JsoniterScala % "provided"
    )
  )

lazy val server = project
  .in(file("./server"))
  .settings(
    libraryDependencies ++= List(
      "com.linecorp.armeria" %% "armeria-scala" % Versions.Armeria
    ),
    assembly / mainClass := Some("server.ArmeriaServer"),
    assembly / assemblyJarName := "app.jar",

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
  .settings(
    libraryDependencies ++= List(
      "com.raquo" %%% "laminar" % Versions.Laminar,
    ),
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        // .withModuleSplitStyle(
        //   ModuleSplitStyle.SmallModulesFor(List("com.raquo.app")))
    },
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(shared.js)

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
