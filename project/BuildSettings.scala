import sbt._
import sbt.Keys._

import com.github.siasia.WebPlugin.{container, webSettings}
import com.github.siasia.PluginKeys._
import sbtbuildinfo.Plugin._

object BuildSettings {
  val liftVersion = "2.5-RC1"

  val buildTime = SettingKey[String]("build-time")

  // generate a package.json file for grunt
  val genPkg = TaskKey[Unit]("gen-pkg", "Generate a package.json file")
  def genPkgTask = (baseDirectory in Compile, name, version, scalaVersion, buildTime) map {
    (dir, n, v, sv, bt) =>
      val file = dir / "package.json"
      val contents =
      """|{
         |  "name": "%s",
         |  "version": "%s",
         |  "scalaVersion": "%s",
         |  "buildTime": "%s",
         |  "devDependencies": {
         |    "grunt": "~0.4.0",
         |    "grunt-contrib-uglify": "~0.1.2",
         |    "grunt-contrib-concat": "~0.1.3",
         |    "grunt-contrib-jshint": "~0.2.0",
         |    "grunt-contrib-less": "~0.5.0",
         |    "grunt-contrib-watch": "~0.3.1",
         |    "grunt-contrib-jasmine": "~0.3.3"
         |  }
         |}
         |""".format(n, v, sv, bt).stripMargin
      IO.write(file, contents)
      ()
  }

  // call grunt init - requires npm be installed
  val gruntInit = TaskKey[Int]("grunt-init", "Initialize project for grunt")
  def gruntInitTask = (baseDirectory in Compile) map { dir =>
    Process(Seq("npm", "install"), dir) !
  }

  // call grunt compile
  val gruntCompile = TaskKey[Int]("grunt-compile", "Call the grunt compile command")
  def gruntCompileTask = (baseDirectory in Compile) map { dir =>
    Process(Seq("grunt", "compile"), dir) !
  }

  // call grunt compress
  val gruntCompress = TaskKey[Int]("grunt-compress", "Call the grunt compress command")
  def gruntCompressTask = (baseDirectory in Compile) map { dir =>
    Process(Seq("grunt", "compress"), dir) !
  }

  val basicSettings = Defaults.defaultSettings ++ Seq(
    version := "0.1.0",
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

  val exampleSettings = basicSettings ++
    webSettings ++
    buildInfoSettings ++
    seq(
      buildTime := System.currentTimeMillis.toString,

      // build-info
      buildInfoKeys ++= Seq[BuildInfoKey](buildTime),
      buildInfoPackage := "code",
      sourceGenerators in Compile <+= buildInfo,

      // grunt tasks
      genPkg <<= genPkgTask,
      gruntInit <<= gruntInitTask dependsOn genPkg,
      gruntCompile <<= gruntCompileTask,
      gruntCompress <<= gruntCompressTask,

      // dependencies
      compile <<= (compile in Compile) dependsOn (genPkg, gruntCompile),
      (start in container.Configuration) <<= (start in container.Configuration) dependsOn (genPkg, gruntCompile),
      Keys.`package` <<= (Keys.`package` in Compile) dependsOn gruntCompress,

      // add managed resources, where grunt publishes to, to the webapp
      (webappResources in Compile) <+= (resourceManaged in Compile)
    )

  lazy val noPublishing = seq(
    publish := (),
    publishLocal := ()
  )
}

