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
  * Common functionality for JavaScript classes.
  */

trait JsClassLike {
  def name: String
  def varName: String

  /**
    * JsExp to init a class
    */
  def init(params: JsExp*): JsCmd =
    SetExp(
      JsVar(varName),
      JsExtras.CallNew(name, params:_*)
    )

  /**
    * JsExp a function on the JavaScript variable that is an instance
    * of the JavaScript view model class.
    */
  def call(func: String, params: JsExp*): Call =
    Call("%s.%s".format(varName, func), params:_*)
}

/**
  * A knockout.js JavaScript class
  */
case class JsClass(name: String, varName: String) extends JsClassLike

case class KoClass(name: String, varName: String, elementId: String) extends JsClassLike {
  /**
    * Call to bind a knockout view model
    */
  def applyBindings: Call =
    Call("ko.applyBindings", JsVar(varName), Call("document.getElementById", elementId))

  override def init(params: JsExp*): JsCmd =
    super.init(params:_*) & applyBindings
}
