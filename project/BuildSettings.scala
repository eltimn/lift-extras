import sbt._
import sbt.Keys._

import com.github.siasia.WebPlugin.{container, webSettings}
import com.github.siasia.PluginKeys._
import sbtbuildinfo.Plugin._

object BuildSettings {

  val liftVersion = SettingKey[String]("liftVersion", "Full version number of the Lift Web Framework")

  val liftEdition = SettingKey[String]("liftEdition", "Lift Edition (short version number to append to artifact name)")

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
         |    "grunt": "~0.4.1",
         |    "grunt-contrib-uglify": "~0.1.2",
         |    "grunt-contrib-concat": "~0.1.3",
         |    "grunt-contrib-jshint": "~0.2.0",
         |    "grunt-contrib-less": "~0.5.0",
         |    "grunt-contrib-watch": "~0.3.1",
         |    "grunt-contrib-jasmine": "~0.3.3"
         |  }
         |}
         |""".format(n, v.replaceAllLiterally("-SNAPSHOT", ""), sv, bt).stripMargin
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
    name := "extras",
    organization := "net.liftmodules",
    version := "0.1",
    liftVersion <<= liftVersion ?? "2.5-RC2",
    liftEdition <<= liftVersion apply { _.substring(0,3) },
    name <<= (name, liftEdition) { (n, e) =>  n + "_" + e },
    scalaVersion := "2.10.0",
    crossScalaVersions := Seq("2.9.2", "2.9.1-1", "2.9.1", "2.10.0"),
    scalacOptions <<= scalaVersion map { sv: String =>
      if (sv.startsWith("2.10."))
        Seq("-deprecation", "-unchecked", "-feature", "-language:postfixOps", "-language:implicitConversions")
      else
        Seq("-deprecation", "-unchecked")
    }
  )

  val publishSettings = seq(
    resolvers += "CB Central Mirror" at "http://repo.cloudbees.com/content/groups/public",
    resolvers += "Sonatype OSS Release" at "http://oss.sonatype.org/content/repositories/releases",
    resolvers += "Sonatype Snapshot" at "http://oss.sonatype.org/content/repositories/snapshots",

    publishTo <<= version { _.endsWith("SNAPSHOT") match {
      case true  => Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
      case false => Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
    }},

    credentials += Credentials( file("sonatype.credentials") ),
    credentials += Credentials( file("/private/liftmodules/sonatype.credentials") ),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>https://github.com/eltimn/lift-mongoauth</url>
      <licenses>
        <license>
            <name>Apache 2.0 License</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
            <distribution>repo</distribution>
          </license>
       </licenses>
       <scm>
          <url>git@github.com:eltimn/lift-mongoauth.git</url>
          <connection>scm:git:git@github.com:eltimn/lift-mongoauth.git</connection>
       </scm>
       <developers>
          <developer>
            <id>eltimn</id>
            <name>Tim Nelson</name>
            <url>http://eltimn.com/</url>
        </developer>
       </developers>
     )
  )

  val exampleSettings =
    basicSettings ++
    webSettings ++
    buildInfoSettings ++
    noPublishing ++
    seq(
      name := "extras-example",
      buildTime := System.currentTimeMillis.toString,

      // build-info
      buildInfoKeys ++= Seq[BuildInfoKey](buildTime),
      buildInfoPackage := "code",
      sourceGenerators in Compile <+= buildInfo,

      // grunt tasks
      genPkg <<= genPkgTask,
      gruntInit <<= gruntInitTask dependsOn genPkg,
      gruntCompile <<= gruntCompileTask dependsOn genPkg,
      gruntCompress <<= gruntCompressTask dependsOn genPkg,

      // dependencies
      // compile <<= (compile in Compile) dependsOn (genPkg, gruntCompile),
      // (start in container.Configuration) <<= (start in container.Configuration) dependsOn (genPkg, gruntCompile),
      Keys.`package` <<= (Keys.`package` in Compile) dependsOn gruntCompress,

      // add managed resources, where grunt publishes to, to the webapp
      (webappResources in Compile) <+= (resourceManaged in Compile)
    )

  lazy val noPublishing = seq(
    publish := (),
    publishLocal := ()
  )
}

