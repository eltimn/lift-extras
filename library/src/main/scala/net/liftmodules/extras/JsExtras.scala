package net.liftmodules.extras

import net.liftweb._
import common._
import http.{Factory, JsonContext, S, SHtml}
import http.js._
import JsCmds._
import JE._
import json._
import util.CssSel
import util.Helpers._

object JsExtras extends Factory {

  def defaultParseJsonFunc(in: String, func: JValue => JsCmd): JsCmd = tryo(parse(in)) match {
    case Full(jv) => func(jv)
    case Failure(msg, _, _) => LiftNotice.error(msg).asJsCmd
    case Empty => LiftNotice.warning("Empty").asJsCmd
  }

  /**
    * An anonymous JavaScript function that calls the callback function via Ajax and
    * executes the return JsCmd on the client.
    */
  object AjaxCallbackAnonFunc {
    def apply(callback: () => JsCmd): AnonFunc = {
      val funcCmd = S.fmapFunc(S.SFuncHolder(s => callback()))(name =>
        SHtml.makeAjaxCall(JsRaw("'" + name + "=true'"))
      )
      AnonFunc(funcCmd)
    }
  }

  /**
    * An anonymous JavaScript function that takes JSON data as a parameter and sends it
    * to a callback function via Ajax and executes the return JsCmd on the client.
    */
  object JsonCallbackAnonFunc {
    def apply(callback: JValue => JsCmd): AnonFunc = {
      val funcCmd = S.fmapFunc(S.SFuncHolder(s => LiftExtras.parseJsonFunc.vend(s, callback)))(name =>
        SHtml.makeAjaxCall(JsRaw("'" + name + "=' + encodeURIComponent(JSON.stringify(data))"))
      )
      AnonFunc("data", funcCmd)
    }
  }
}

