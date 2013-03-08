package code
package snippet

import java.text.SimpleDateFormat
import java.util.Date

import net.liftweb._
import common._
import util._
import util.Helpers._

object Assets {
  import Props.RunModes._

  // do we load the minified version
  lazy val minified: String =
    if (Props.mode == Development) ""
    else ".min"

  lazy val artifactName = "%s-%s-%s%s".format(BuildInfo.name, BuildInfo.version, BuildInfo.buildTime, minified)

  // prevents browser caching in dev
  def tail: String =
    if (Props.mode == Development) "?"+System.currentTimeMillis.toString
    else ""

  def css = "* [href]" #> "/assets/%s.css%s".format(artifactName, tail)
  def js = "* [src]" #> "/assets/%s.js%s".format(artifactName, tail)
}
