package net.liftmodules.extras
package snippet

import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date

import net.liftweb._
import common._
import http.S
import json._
import util._
import util.Helpers._

/**
  * To be used with grunt-hash.
  */
@deprecated("No longer supported. Copy to your project if still using.", "0.7")
trait HashedAssets {
  import Props.RunModes._

  /**
    * Load the assets map produced by grunt-hash
    */
  protected lazy val assetsMap: Map[String, String] = {
    (tryo{ new InputStreamReader(getClass.getResourceAsStream(LiftExtras.mappingsUri.vend)) }
      .filter(_ ne null)
      .flatMap { s => tryo(JsonParser.parse(s)) }) match
    {
      case Full(jo: JObject) => jo.values.mapValues(_.toString)
      case _ => Map.empty
    }
  }

  protected def artifactPath(ext: String): String = {
    val artifact =
      if (Props.mode == Development) "%s.%s".format(LiftExtras.artifactName.vend, ext)
      else {
        val art = "%s.min.%s".format(LiftExtras.artifactName.vend, ext)
        assetsMap.getOrElse(art, art)
      }

    "%s/%s".format(LiftExtras.artifactServer.vend, (LiftExtras.artifactPath.vend :+ artifact).mkString("/"))
  }

  protected lazy val cssPath: String = artifactPath("css")
  protected lazy val jsPath: String = artifactPath("js")

  def css = "* [href]" #> cssPath
  def js = "* [src]" #> jsPath
}

@deprecated("No longer supported. Copy to your project if still using.", "0.7")
object HashedAssets extends HashedAssets
