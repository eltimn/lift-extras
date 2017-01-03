package net.liftmodules.extras
package snippet

import scala.io.Source
import scala.xml._

import net.liftweb._
import common._
import http.{DispatchSnippet, LiftRules, S}
import http.js._
import json._
import util._
import util.Helpers._

import JsCmds._
import NgJE._

/**
  * To be used with grunt-hash. See gruntfile.js.
  */
@deprecated("No longer supported. Copy to your project if still using.", "0.7")
trait AssetLoader extends DispatchSnippet with Loggable {
  import Props.RunModes._

  def dispatch = {
    case "css" => {
      if (Props.mode == Development) cssdev
      else css
    }
    case "js" => {
      if (Props.mode == Development) jsdev
      else js
    }
  }

  /**
    * Load the assets map produced by grunt-hash
    */
  protected lazy val assetsMap = AssetLoader.buildAssetsMap(LiftExtras.mappingsUri.vend)

  /**
    * Calculate the whole path to the artifact.
    */
  protected def artifactPath(ext: String): String = {
    val artifact =
      if (Props.mode == Development) "%s.%s".format(LiftExtras.artifactName.vend, ext)
      else {
        val art = "%s.min.%s".format(LiftExtras.artifactName.vend, ext)
        assetsMap.getOrElse(art, art)
      }

    "%s/%s".format(LiftExtras.assetServer.vend, (LiftExtras.artifactPath.vend :+ artifact).mkString("/"))
  }

  protected lazy val cssPath: String = artifactPath("css")
  protected lazy val jsPath: String = artifactPath("js")

  def css = "* [href]" #> cssPath
  def js = "* [src]" #> jsPath

  def cssdev(in: NodeSeq): NodeSeq = {
    val vendorSources: Seq[String] = (LiftExtras.cssSources.vend
      .flatMap { s => AssetLoader.sourcesListFromResource(s) })

    val srcs: Seq[String] = "/assets/app_styles.css" +: vendorSources

    Group(srcs.map { src =>
      <link href={src} rel="stylesheet"></link>
    })
  }

  def jsdev(in: NodeSeq): NodeSeq = {
    val srcs: Seq[Elem] = LiftExtras.jsSources.vend
      .flatMap { s => AssetLoader.sourcesListFromResource(s) }
      .map { src => <script src={src}></script> }

    Group(srcs)
  }
}

@deprecated("No longer supported. Copy to your project if still using.", "0.7")
object AssetLoader extends AssetLoader with SimpleInjector with Loggable {

  def sourcesListFromResource(uri: String): List[String] = {
    LiftRules.doWithResource(uri) { is =>
      Source.fromInputStream(is, "UTF-8").getLines.toList
    } openOr Nil
  }

  def buildAssetsMap(uri: String): Map[String, String] = {
    (
      LiftRules
        .loadResourceAsString(uri)
        .flatMap { s => tryo(JsonParser.parse(s)) }
    ) match {
      case Full(jo: JObject) => jo.values.mapValues(_.toString)
      case _ => Map.empty
    }
  }
}
