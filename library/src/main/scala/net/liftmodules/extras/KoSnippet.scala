package net.liftmodules.extras

import net.liftweb._
import common._
import http.{NoticeType, JsonContext, SHtml, S}
import http.js._
import JsCmds._
import JE._
import json._
import util.Helpers.tryo

/**
  * A snippet that uses a Knockout JavaScipt module. Provides functions to initialize module and bind to ko.
  */
trait KoSnippet extends JsModSnippet {
  def elementId: String

  /**
    * JsCmd to bind a knockout view model
    */
  def KoBind: JsCmd =
    Call("ko.applyBindings", JsVar(moduleName), Call("document.getElementById", elementId))

  def KoInitBind(params: JsExp*): JsCmd = {
    JsModInit(params:_*) & KoBind
  }
}
