import sbt._
import Keys._
import metadata.{ developer, project => meta }

object AStarBuild extends Build {

  lazy val astar = (project in file(".")
    settings (commonSettings: _*)
    settings (Publish.settings: _*)
  )

  lazy val commonSettings = Seq(
    moduleName   := meta.name,
    organization := developer.org,

    crossScalaVersions  := Seq("2.10.5", "2.11.7"),

    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature"
    ),

    javacOptions ++= Seq(
      "-Xlint:unchecked"
    ),

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.4" % "test",
      "junit" % "junit" % "4.12" % "test",
      "com.novocode" % "junit-interface" % "0.11" % "test"
    )
  )
}
