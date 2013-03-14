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

trait KoLike extends JsModLike {
  /**
    * Convert the snippet/comet class name to an id to be used on the html
    * element that ko will be bound to.
    *
    * Ex: code.snippet.BsNotices -> bs-notices
    */
  lazy val elementId: String = {
    splitCamelCase(getClass.getName.split("\\.").toList.last.replace("$", ""))
  }

  /**
    * JsCmd to bind a knockout view model
    */
  def KoBind: JsCmd =
    Call("ko.applyBindings", JsVar(moduleName), Call("document.getElementById", elementId))

  def KoInitBind(params: JsExp*): JsCmd = {
    JsModInit(params:_*) & KoBind
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
trait KoSnippet extends KoLike with JsModSnippet

/**
  * A comet that uses a Knockout JavaScipt module.
  */
trait KoComet extends KoLike with JsModComet

/**
  * A snippet that uses a Knockout JavaScipt module and includes a script tag that loads the js module.
  */
trait KoSnippetWithScript extends KoLike with JsModSnippet with RenderWithScript

/**
  * A comet that uses a Knockout JavaScipt module and includes a script tag that loads the js module.
  */
trait KoCometWithScript extends KoLike with JsModComet with RenderWithScript
