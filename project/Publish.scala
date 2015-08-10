import sbt._
import Keys._
import metadata.oss

object Publish {

  lazy val settings = Seq(
    publishMavenStyle := true,
    publishTo <<= version { v =>
      if (v.trim endsWith "SNAPSHOT")
        Some(oss.snapshots)
      else
        Some(oss.staging)
    },
    publishArtifact in Test := false,
    pomIncludeRepository := (_ => false),
    pomExtra := metadata.project.pom
  )
}
