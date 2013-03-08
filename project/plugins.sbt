resolvers += "web-plugin.repo" at "http://siasia.github.com/maven2"

libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % ("0.12.0-0.2.11.1"))

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.2.2")
