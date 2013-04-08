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
    .settings(libraryDependencies <++= (liftVersion) { liftVersion =>
      Seq(
        "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
        "org.scalatest" %% "scalatest" % "1.9.1" % "test"
      )
    })

  lazy val example = Project("lift-extras-example", file("example"))
    .dependsOn(library)
    .settings(exampleSettings: _*)
    .settings(libraryDependencies <++= (liftVersion) { liftVersion =>
      Seq(
        "net.liftweb" %% "lift-record" % liftVersion % "compile",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
        "ch.qos.logback" % "logback-classic" % "1.0.3",
        "org.scalatest" %% "scalatest" % "1.9.1" % "test"
      )
    })
}
