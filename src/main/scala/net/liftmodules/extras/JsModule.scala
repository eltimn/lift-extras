package net.liftmodules.extras

import net.liftweb._
import common._
import http._
import http.js._
import JsCmds._
import JE._
import json._
import util.Helpers.tryo

/**
  * Common functionality for JavaScript modules.
  */
trait JsModuleLike {
  def name: String
  /**
    * JsCmd to init a module
    */
  def init(params: JsExp*): JsCmd =
    Call("%s.init".format(name), params:_*)

  /**
    * Call a function on the JavaScript module.
    */
  def call(func: String, params: JsExp*): Call =
    Call("%s.%s".format(name, func), params:_*)
}

/**
  * A JavaScript module
  */
case class JsModule(name: String) extends JsModuleLike

/**
  * A knockout.js JavaScript module
  */
case class KoModule(name: String, elementId: String) extends JsModuleLike {
  def applyBindings: Call =
    Call("ko.applyBindings", JsVar(name), Call("document.getElementById", elementId))

  override def init(params: JsExp*): JsCmd = {
    super.init(params:_*) & applyBindings
  }
}
