name := "kmeans"

/// projects

lazy val core = crossProject
  .in(file("modules/core"))
  .settings(commonSettings)

lazy val coreJS = core.js
lazy val coreJVM = core.jvm

lazy val gui = crossProject
  .in(file("modules/gui"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.2"
    ),
    // This is an application with a main method
    scalaJSUseMainModuleInitializer := true
  )

lazy val guiJS = gui.js

/// settings

lazy val commonSettings = Def.settings(
  compileSettings
)

lazy val compileSettings = Def.settings(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    //"-Xfatal-warnings",
    "-Xfuture",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"
  )
)

/// commands

def addCommandsAlias(name: String, cmds: Seq[String]) =
  addCommandAlias(name, cmds.mkString(";", ";", ""))

addCommandsAlias("validate",
                 Seq(
                   "clean",
                   "scalafmt::test",
                   "test:scalafmt::test",
                   "sbt:scalafmt::test",
                   "test",
                   "doc"
                 ))
