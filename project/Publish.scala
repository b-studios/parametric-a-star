import sbt._
import Keys._
import metadata.oss

object Publish {

  lazy val settings = Seq(
    credentialsSetting,
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

  lazy val credentialsSetting =
    credentials ++= (for {
      user <- sys.env.get("SONATYPE_USER")
      pass <- sys.env.get("SONATYPE_PASS")
    } yield Credentials(
      "Sonatype Nexus Repository Manager",
      "oss.sonatype.org",
      user,
      pass)
    ).toSeq
}
