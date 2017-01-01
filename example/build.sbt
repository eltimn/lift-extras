import LiftModule.liftVersion

libraryDependencies ++=
  "net.liftweb" %% "lift-webkit" % liftVersion.value % "compile" ::
  "net.liftweb" %% "lift-record" % liftVersion.value % "compile" ::
  "org.scalatest" %% "scalatest" % "3.0.1" % "test" ::
  Nil
