import LiftModule.{liftVersion, liftEdition}

name := "extras"
organization := "net.liftmodules"
liftVersion := "3.0.1"
liftEdition := liftVersion.value.split('.').take(2).mkString(".")
moduleName := name.value + "_" + liftEdition.value

scalaVersion := "2.12.1"
crossScalaVersions := Seq("2.12.1", "2.11.8")
scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++=
  "net.liftweb" %% "lift-webkit" % liftVersion.value % "provided" ::
  "org.scalatest" %% "scalatest" % "3.0.1" % "test" ::
  Nil

enablePlugins(GitVersioning)

// Add the alerts js files to jar
resourceGenerators in Compile += Def.task {
  val jsDir = (sourceDirectory in Compile).value /
    "assets" /
    "js"

  val webjarDir = (resourceManaged in Compile).value /
    "META-INF" /
    "resources" /
    "webjars" /
    s"lift-${name.value}" /
    s"${version.value}"

  Seq("bsAlerts", "bsFormAlerts").flatMap { f =>
    val destFile = s"jquery.${f}.js"
    val destMinFile = s"jquery.${f}.min.js"
    IO.copyFile(jsDir / destFile, webjarDir / destFile)
    IO.copyFile(jsDir / destMinFile, webjarDir / destMinFile)
    Seq(webjarDir / destFile, webjarDir / destMinFile)
  }
}.taskValue
