organization := "net.liftmodules"

name := "lift-utils"

liftVersion <<= liftVersion ?? "2.5-RC1"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.0"

libraryDependencies <++= (liftVersion) { liftVersion =>
  Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "ch.qos.logback" % "logback-classic" % "1.0.3" % "provided"
  )
}

scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-language:postfixOps")
