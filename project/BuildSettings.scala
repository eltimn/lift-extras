import sbt._
import sbt.Keys._

import com.github.siasia.WebPlugin.webSettings
import com.github.siasia.PluginKeys._
import sbtclosure.SbtClosurePlugin._
import less.Plugin._

object BuildSettings {
  val liftVersion = "2.5-RC1"

  lazy val basicSettings = seq(
    version := "%s-0.1-SNAPSHOT".format(liftVersion),
    organization := "net.liftmodules",
    scalaVersion := "2.9.2",
    crossScalaVersions := Seq("2.9.2", "2.10.0"),
    scalacOptions <<= scalaVersion map { sv: String =>
      if (sv.startsWith("2.10."))
        Seq("-deprecation", "-unchecked", "-feature", "-language:postfixOps", "-language:implicitConversions")
      else
        Seq("-deprecation", "-unchecked")
    }
  )

  lazy val exampleSettings = basicSettings ++
    webSettings ++
    lessSettings ++
    closureSettings ++
    seq(
      // less-sbt
      (LessKeys.filter in (Compile, LessKeys.less)) := "styles*.less",
      (LessKeys.mini in (Compile, LessKeys.less)) := true,

      // Closure
      (ClosureKeys.prettyPrint in (Compile, ClosureKeys.closure)) := false,

      // add managed resources, where less and closure publish to, to the webapp
      (webappResources in Compile) <+= (resourceManaged in Compile)
    )

  lazy val noPublishing = seq(
    publish := (),
    publishLocal := ()
  )
}
