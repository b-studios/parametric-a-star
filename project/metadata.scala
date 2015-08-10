import sbt._
import Keys._

object metadata {

  object project {
    val url     = "https://github.com/b-studios/parametric-a-star"
    val name    = "parametric-a-star"
    val version = "0.1-SNAPSHOT"
    val pom     = xml.NodeSeq.fromSeq(Seq(
      <url>{ url }</url>,
      developer.pom,
      license.pom,
      scm.pom))
  }
  object developer {
    val id   = "b-studios"
    val name = "Jonathan Brachthäuser"
    val org  = "de.b-studios"
    val pom =
      <developers>
        <developer>
          <id>{ id }</id>
          <name>{ name }</name>
        </developer>
      </developers>
  }
  object license {
    val url   = "http://opensource.org/licenses/MIT"
    val name  = "MIT License"
    val distr = "repo"
    val pom   =
      <licenses>
        <license>
          <name>{ name }</name>
          <url>{ url }</url>
          <distribution>{ distr }</distribution>
        </license>
      </licenses>
  }
  object scm {
    val url  = project.url
    val conn = s"scm:git:$url"
    val pom =
      <scm>
        <url>{ url }</url>
        <connection>{ conn }</connection>
      </scm>
  }
  object oss {
    val snapshots = "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
    val staging   = "Sonatype OSS Staging" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
  }
}
