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

trait JsClsLike extends JsName {
  def jsClsName = classNameToJsName
  def jsVarName: String

  // User defined render
  protected def doRender(in: NodeSeq): NodeSeq

  /**
    * JsCmd to init a module
    */
  def JsClassInit(params: JsExp*): JsCmd =
    SetExp(
      JsVar(jsVarName),
      JsExtras.CallNew(jsClsName, params:_*)
    )
}


/**
  * A snippet that uses a JavaScipt class.
  */
trait JsClsSnippet extends JsModLike {
  def render(in: NodeSeq): NodeSeq = doRender(in)
}

/**
  * A comet that uses a JavaScipt class.
  */
trait JsClsComet extends JsModLike {
  def render: RenderOut = new RenderOut(doRender(NodeSeq.Empty))
}
