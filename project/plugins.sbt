resolvers += "web-plugin.repo" at "http://siasia.github.com/maven2"

libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % ("0.12.0-0.2.11.1"))

addSbtPlugin("org.scala-sbt" % "sbt-closure" % "0.1.3")

addSbtPlugin("me.lessis" % "less-sbt" % "0.1.10")
