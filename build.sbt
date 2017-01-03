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
