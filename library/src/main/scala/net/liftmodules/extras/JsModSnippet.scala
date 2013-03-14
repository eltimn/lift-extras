package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http._
import http.js._
import JsCmds._
import JE._
import json._
import util.Helpers.tryo

trait JsModLike extends JsName {
  lazy val moduleName = classNameToJsName

  // User defined render
  protected def doRender(in: NodeSeq): NodeSeq

  /**
    * JsCmd to init a module
    */
  def JsModInit(params: JsExp*): JsCmd =
    Call("%s.init".format(moduleName), params:_*)

  /**
    * Call a function on the JavaScript model.
    */
  def CallJsMod(func: String, params: JsExp*): JsCmd =
    Call("%s.%s".format(moduleName, func), params:_*)
}


/**
  * A snippet that uses a JavaScipt module.
  */
trait JsModSnippet extends JsModLike {
  def render(in: NodeSeq): NodeSeq = doRender(in)
}

/**
  * A comet that uses a JavaScipt module.
  */
trait JsModComet extends JsModLike {
  def render: RenderOut = new RenderOut(doRender(NodeSeq.Empty))
}
