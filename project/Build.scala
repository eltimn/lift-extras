import sbt._
import sbt.Keys._

object LiftModuleBuild extends Build {

  import BuildSettings._

  lazy val root = Project("root", file("."))
    .aggregate(library, example)
    .settings(basicSettings:_*)
    .settings(noPublishing:_*)

  lazy val library = Project("lift-extras", file("library"))
    .settings(basicSettings:_*)
    .settings(publishSettings:_*)
    .settings(libraryDependencies <++= (scalaVersion, liftVersion) { (scalaVersion, liftVersion) =>
      val scalaTestVer = scalaVersion match {
        case v if (v.startsWith("2.10") || v.startsWith("2.11")) => "2.2.1"
        case _ => "1.9.2"
      }
      Seq(
        "net.liftweb" %% "lift-webkit" % liftVersion % "provided",
        "org.scalatest" %% "scalatest" % scalaTestVer % "test"
      )
    })
    .settings(Seq(
      // Necessary beginning with sbt 0.13, otherwise Lift editions get messed up.
      // E.g. "2.5" gets converted to "2-5"
      moduleName := name.value
    ))

  lazy val example = Project("example", file("example"))
    .dependsOn(library)
    .settings(exampleSettings: _*)
    .settings(libraryDependencies <++= (scalaVersion, liftVersion) { (scalaVersion, liftVersion) =>
      val scalaTestVer = scalaVersion match {
        case v if (v.startsWith("2.10") || v.startsWith("2.11")) => "2.2.1"
        case _ => "1.9.2"
      }
      Seq(
        "net.liftweb" %% "lift-record" % liftVersion % "compile",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.2.v20140723" % "container",
        "ch.qos.logback" % "logback-classic" % "1.1.2",
        "org.scalatest" %% "scalatest" % scalaTestVer % "test"
      )
    })
}
