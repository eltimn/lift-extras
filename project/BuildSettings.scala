import sbt._
import sbt.Keys._

import com.github.siasia.WebPlugin.{container, webSettings}
import com.github.siasia.PluginKeys._
import sbtbuildinfo.Plugin._
import cloudbees.Plugin._

object BuildSettings {

  val liftVersion = SettingKey[String]("liftVersion", "Full version number of the Lift Web Framework")
  val liftEdition = SettingKey[String]("liftEdition", "Lift Edition (short version number to append to artifact name)")

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

  // call grunt test
  val gruntTest = TaskKey[Int]("grunt-test", "Call the grunt test command")
  def gruntTestTask = (baseDirectory in Compile) map { dir =>
    Process(Seq("grunt", "test"), dir) !
  }

  val basicSettings = Defaults.defaultSettings ++ Seq(
    name := "extras",
    organization := "net.liftmodules",
    version := "0.3-SNAPSHOT",
    liftVersion <<= liftVersion ?? "2.6-M1",
    liftEdition <<= liftVersion apply { _.substring(0,3) },
    name <<= (name, liftEdition) { (n, e) =>  n + "_" + e },
    scalaVersion := "2.10.3",
    crossScalaVersions := Seq("2.9.2", "2.9.1-1", "2.9.1", "2.10.3"),
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
      <url>https://github.com/eltimn/lift-extras</url>
      <licenses>
        <license>
            <name>Apache 2.0 License</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
            <distribution>repo</distribution>
          </license>
       </licenses>
       <scm>
          <url>git@github.com:eltimn/lift-extras.git</url>
          <connection>scm:git:git@github.com:eltimn/lift-extras.git</connection>
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
    cloudBeesSettings ++
    seq(
      name := "extras-example",
      CloudBees.applicationId := Some("lift-extras-example"),

      // build-info
      buildInfoPackage := "code",
      sourceGenerators in Compile <+= buildInfo,

      // grunt tasks
      gruntInit <<= gruntInitTask,
      gruntCompile <<= gruntCompileTask,
      gruntCompress <<= gruntCompressTask,
      gruntTest <<= gruntTestTask,

      // dependencies
      compile <<= (compile in Compile) dependsOn gruntCompile,
      // (start in container.Configuration) <<= (start in container.Configuration) dependsOn gruntCompile,
      Keys.`package` <<= (Keys.`package` in Compile) dependsOn gruntCompress,
      test <<= (test in Test) dependsOn gruntTest,

      // add directory where grunt publishes to, to the webapp
      (webappResources in Compile) <+= (baseDirectory) { _ / "grunt-build" / "out" },
      // add assets.json to classpath
      (unmanagedResourceDirectories in Compile) <+= (baseDirectory) { _ / "grunt-build" / "hash" }
    )

  lazy val noPublishing = seq(
    publish := (),
    publishLocal := ()
  )
}

