name := "kmeans"

/// projects

lazy val core = crossProject
  .in(file("modules/core"))

lazy val coreJS = core.js
lazy val coreJVM = core.jvm

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
