bintrayReleaseOnPublish in ThisBuild := false

pomExtra := {
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
}

publishArtifact in Test := false
homepage := Some(url("https://github.com/eltimn/lift-extras"))
licenses := Seq(("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.txt")))
publishTo := Some("eltimn-maven" at "https://api.bintray.com/maven/eltimn/maven/lift-extras/;publish=1")
