package net.liftmodules.extras

import net.liftweb._
import common._
import http.{JsonContext, S, SHtml}
import http.js._
import JsCmds._
import JE._
import json._
import util.CssSel
import util.Helpers._

object SHtmlExtras extends SHtmlExtras {
  def parseJValue(in: String, func: JValue => JsCmd): JsCmd = tryo(parse(in)) match {
    case Full(jv) => func(jv)
    case Failure(msg, _, _) => JsNotice.error(msg).asJsCmd
    case Empty => JsNotice.warning("Empty").asJsCmd
  }
}

trait SHtmlExtras {
  /**
    * Creates an anonymous JavaScript function that makes a jsonCall.
    */
  def jsonCallbackFunc(calcName: String, func: JValue => JsCmd): JsExp =
    AnonFunc(SHtml.jsonCall(Call(calcName), func).cmd)

  /**
    * Creates an anonymous JavaScript function that makes a jsonCall with a JsonContext.
    */
  def jsonCallbackFunc(calcName: String, context: JsonContext, func: JValue => JValue): JsExp =
    AnonFunc(SHtml.jsonCall(Call(calcName), context, func).cmd)

  /**
    * Creates a callback function that allows calling liftAjax directly with JSON data.
    */
  def jsonFuncName(func: JValue => JsCmd): String = {
    S.fmapFunc(S.SFuncHolder(s => LiftExtras.parseJValueFunc.vend(s, func)))(name =>
      name
    )
  }

  /**
    * Creates a callback function that allows calling liftAjax directly with no data.
    */
  def ajaxFuncName(func: () => JsCmd): String = {
    S.fmapFunc(S.SFuncHolder(s => func()))(name =>
      name
    )
  }
}
