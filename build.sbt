name := "kmeans"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "0.9.0"
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
