import sbt._
import sbt.Keys._

import com.github.siasia.WebPlugin.{container, webSettings}
import com.github.siasia.PluginKeys._
import sbtbuildinfo.Plugin._
import cloudbees.Plugin._

object BuildSettings {

  val resolutionRepos = Seq(
    "Sonatype Snapshot" at "http://oss.sonatype.org/content/repositories/snapshots"
  )

  val liftVersion = SettingKey[String]("liftVersion", "Full version number of the Lift Web Framework")
  val liftEdition = SettingKey[String]("liftEdition", "Lift Edition (short version number to append to artifact name)")

  // call grunt init - requires npm be installed
  val gruntInit = TaskKey[Int]("grunt-init", "Initialize project for grunt")
  def gruntInitTask = (baseDirectory in Compile) map { dir =>
    Process(Seq("npm", "install"), dir) !
  }

  // call grunt
  val gruntDefault = TaskKey[Int]("grunt-default", "Call the grunt default command")
  def gruntDefaultTask = (baseDirectory in Compile, streams) map { (dir, s) =>
    val code: Int = Process(Seq("grunt", "default"), dir) ! s.log
    s.log.info("grunt-init: "+code.toString)
    if (code != 0) {
      sys.error("grunt-init failed.")
    }
    code
  }

  // call grunt build
  val gruntBuild = TaskKey[Int]("grunt-build", "Call the grunt build command")
  def gruntBuildTask = (baseDirectory in Compile) map { dir =>
    Process(Seq("grunt", "build"), dir) !
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

  // call grunt clean
  val gruntClean = TaskKey[Int]("grunt-clean", "Call the grunt clean command")
  def gruntCleanTask = (baseDirectory in Compile) map { dir =>
    Process(Seq("grunt", "clean"), dir) !
  }

  val basicSettings = Defaults.defaultSettings ++ Seq(
    name := "extras",
    organization := "net.liftmodules",
    version := "0.4-LOCAL",
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
    },
    resolvers ++= resolutionRepos
  )

  val gruntSettings = Seq(
    gruntInit <<= gruntInitTask,
    gruntDefault <<= gruntDefaultTask,
    gruntBuild <<= gruntBuildTask,
    gruntCompress <<= gruntCompressTask,
    gruntTest <<= gruntTestTask,
    gruntClean <<= gruntCleanTask

    // dependencies
    // compile <<= (compile in Compile) dependsOn gruntBuild,
    // (start in container.Configuration) <<= (start in container.Configuration) dependsOn gruntBuild,
    // clean <<= clean dependsOn gruntClean,
    // Keys.`package` <<= (Keys.`package` in Compile) dependsOn gruntCompress,
    // test <<= (test in Test) dependsOn gruntTest
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
    gruntSettings ++
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

      // dependencies
      compile <<= (compile in Compile) dependsOn gruntBuild,
      Keys.`package` <<= (Keys.`package` in Compile) dependsOn gruntCompress,
      test <<= (test in Test) dependsOn gruntTest,

      // add javascript and css source files to the webapp, for development
      (webappResources in Compile) <+= (baseDirectory) { _ / "src" / "frontend" },
      (webappResources in Compile) <+= (target) { _ / "grunt" / "build" },

      // add grunt generated resources to the classpath
      (unmanagedResourceDirectories in Compile) <+=
        (target) { _ / "grunt" / "resources" },

      // include assets in the packaged jar
      resourceGenerators in Compile <+= (resourceManaged, target) map { (managedBase, t) =>
        val gruntDist = t / "grunt" / "dist"
        for {
          (from, to) <- gruntDist ** "*" x rebase(gruntDist, managedBase / "main" / "webapp")
        } yield {
          Sync.copy(from, to)
          to
        }
      }
    )

  lazy val noPublishing = seq(
    publish := (),
    publishLocal := ()
  )
}

