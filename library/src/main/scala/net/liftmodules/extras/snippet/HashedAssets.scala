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
trait HashedAssets {
  import Props.RunModes._

  private val assetsMap: Map[String, String] = {
    (tryo{ new InputStreamReader(getClass.getResourceAsStream(LiftExtras.mappingsUri.vend)) }
      .filter(_ ne null)
      .flatMap { s => tryo(JsonParser.parse(s)) }) match
    {
      case Full(jo: JObject) => jo.values.mapValues(_.toString)
      case _ => Map.empty
    }
  }

  // prevents browser caching in dev
  private def tail: String =
    if (Props.mode == Development) "?"+System.currentTimeMillis.toString
    else ""

  private def artifactPath(ext: String): String = {
    val artifact =
      if (Props.mode == Development) "%s.%s".format(LiftExtras.artifactName.vend, ext)
      else {
        val art = "%s.min.%s".format(LiftExtras.artifactName.vend, ext)
        assetsMap.getOrElse(art, art)
      }

    "/%s".format((LiftExtras.artifactPath.vend :+ artifact).mkString("/"))
  }

  private val jsPath: String = artifactPath("js")
  private val cssPath: String = artifactPath("css")

  def css = "* [href]" #> "%s%s".format(cssPath, tail)
  def js = "* [src]" #> "%s%s".format(jsPath, tail)
}

object HashedAssets extends HashedAssets
