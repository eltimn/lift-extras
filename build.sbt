organization := "net.liftmodules"

name := "lift-extras"

liftVersion <<= liftVersion ?? "2.5-RC1"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

crossScalaVersions := Seq("2.9.2", "2.10.0")

libraryDependencies <++= (liftVersion) { liftVersion =>
  Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "ch.qos.logback" % "logback-classic" % "1.0.3" % "provided",
    "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )
}

scalacOptions <<= scalaVersion map { sv: String =>
  if (sv.startsWith("2.10."))
    Seq("-deprecation", "-unchecked", "-feature", "-language:postfixOps", "-language:implicitConversions")
  else
    Seq("-deprecation", "-unchecked")
}
