import sbtcrossproject.CrossPlugin.autoImport.crossProject

name := "kmeans"

/// projects

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .in(file("modules/core"))
  .settings(commonSettings)

lazy val coreJS = core.js
lazy val coreJVM = core.jvm

lazy val gui = crossProject(JSPlatform)
  .in(file("modules/gui"))
  .dependsOn(core)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.6"
    ),
    // This is an application with a main method
    scalaJSUseMainModuleInitializer := true
  )

lazy val guiJS = gui.js

def genIndexHtml(sourceDirectory: File, jsScript: File): File = {
  val indexName = "index.html"
  val indexHtml = jsScript.getParentFile / indexName
  val htmlTemplate = sourceDirectory / indexName

  val jsName = jsScript.getName
  val content = IO.read(htmlTemplate).replaceAllLiterally("@JS_NAME@", jsName)

  IO.write(indexHtml, content)
  indexHtml
}

fastOptJS in guiJS := (fastOptJS in (guiJS, Compile)).value.map { file =>
  genIndexHtml((sourceDirectory in guiJS).value, file)
  file
}

fullOptJS in guiJS := (fullOptJS in (guiJS, Compile)).value.map { file =>
  genIndexHtml((sourceDirectory in guiJS).value, file)
  file
}

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
                   "doc",
                   "clean",
                   "guiJS/fullOptJS"
                 ))
