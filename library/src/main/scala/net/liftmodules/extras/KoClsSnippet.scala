package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http.{NoticeType, JsonContext, SHtml, S}
import http.js._
import JsCmds._
import JE._
import json._
import util.Helpers.tryo

trait KoClsLike extends JsClsLike {
  /**
    * Convert the snippet/comet class name to an id to be used on the html
    * element that ko will be bound to.
    *
    * Ex: code.snippet.BsNotices -> bs-notices
    */
  lazy val defaultElementId: String = {
    splitCamelCase(getClass.getName.split("\\.").toList.last.replace("$", ""))
  }

  def elementId: String = defaultElementId

  /**
    * JsCmd to bind a knockout view model
    */
  def KoBind: JsCmd =
    Call("ko.applyBindings", JsVar(jsVarName), Call("document.getElementById", elementId))

  def KoInitBind(params: JsExp*): JsCmd = {
    JsClassInit(params:_*) & KoBind
  }

  // http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
  private def splitCamelCase(s: String): String = {
    s.replaceAll(
      String.format("%s|%s|%s",
         "(?<=[A-Z])(?=[A-Z][a-z])",
         "(?<=[^A-Z])(?=[A-Z])",
         "(?<=[A-Za-z])(?=[^A-Za-z])"
      ),
      "-"
    ).toLowerCase
  }
}

/**
  * A snippet that uses a Knockout JavaScipt module.
  */
trait KoClsSnippet extends KoClsLike with JsClsSnippet

/**
  * A comet that uses a Knockout JavaScipt module.
  */
trait KoClsComet extends KoClsLike with JsClsComet
