import LiftModule.{liftVersion, liftEdition}

lazy val basicSettings: Seq[Setting[_]] = Seq(
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.12.1", "2.11.8"),
  scalacOptions ++= Seq("-unchecked", "-deprecation"),
  parallelExecution in Test := false,
  organization in ThisBuild := "net.liftmodules",
  liftVersion := "3.0.1",
  liftEdition := liftVersion.value.split('.').take(2).mkString("."),
  moduleName := name.value + "_" + liftEdition.value
)

lazy val noPublishing: Seq[Setting[_]] = Seq(
  publish := (),
  publishLocal := ()
)

lazy val root = Project("root", file("."))
  .aggregate(library, example)
  .settings(basicSettings: _*)
  .settings(noPublishing: _*)

lazy val library = Project("extras", file("library"))
  .settings(basicSettings: _*)
  .enablePlugins(GitVersioning)

lazy val example = Project("example", file("example"))
  .dependsOn(library)
  .settings(basicSettings: _*)
  .settings(noPublishing: _*)
