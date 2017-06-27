import LiftModule.{liftVersion, liftEdition}

name := "extras"
organization := "net.liftmodules"
liftVersion := "3.0.1"
liftEdition := liftVersion.value.split('.').take(2).mkString(".")
moduleName := name.value + "_" + liftEdition.value

scalaVersion := crossScalaVersions.value.head
crossScalaVersions := Seq("2.12.2", "2.11.11")
scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies ++=
  "net.liftweb" %% "lift-webkit" % liftVersion.value % "provided" ::
  "org.scalatest" %% "scalatest" % "3.0.1" % "test" ::
  Nil

enablePlugins(GitVersioning)

// Add the alerts js files to jar
resourceGenerators in Compile += Def.task {

  val webjarDir = (resourceManaged in Compile).value /
    "META-INF" /
    "resources" /
    "webjars" /
    s"lift-${name.value}" /
    s"${version.value}"

  def copyFile(file: File): File = {
    val destFile = webjarDir / file.getName
    IO.copyFile(file, destFile)
    destFile
  }

  Seq(
    copyFile(baseDirectory.value / "jquery-bs-alerts/docs/js/jquery.bsAlerts.js"),
    copyFile(baseDirectory.value / "jquery-bs-alerts/dist/jquery.bsAlerts.min.js"),
    copyFile(baseDirectory.value / "jquery-bs-formalerts/docs/js/jquery.bsFormAlerts.js"),
    copyFile(baseDirectory.value / "jquery-bs-formalerts/dist/jquery.bsFormAlerts.min.js")
  )

}.taskValue
