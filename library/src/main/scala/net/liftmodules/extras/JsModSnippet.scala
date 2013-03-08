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

trait JsModLike {
  /**
    * Convert snippet/comet class name into a JavaScript module name. It strips away the
    * packages up to and including snippet/comet, then prepends the module namespace.
    *
    * Ex: code.snippet.notice.MySnippet -> app.views.notice.MySnippet
    */
  lazy val moduleName: String = {
    val clsAsList = getClass.getName.split("\\.").toList
    val ix = clsAsList.indexWhere(s => Seq("snippet", "comet").contains(s))
    val modNameAsList =
      (if (ix >= 0 && clsAsList.length > ix+1) {
        clsAsList.slice(ix+1, clsAsList.length)
      }
      else {
        clsAsList
      }).map(_.replace("$", ""))

    (LiftExtras.moduleNamespace.vend ++ modNameAsList).mkString(".")
  }

  // User defined render
  protected def doRender(in: NodeSeq): NodeSeq

  /**
    * JsCmd to init a module
    */
  def JsModInit(params: JsExp*): JsCmd =
    Call("%s.init".format(moduleName), params:_*)

  /*protected def renderWithScript(in: NodeSeq): NodeSeq = {
    val tail =
      if (includeScript) <tail><script src={"/assets/js/views/%s.js".format(moduleName.replaceAllLiterally(".", "/"))}></script></tail>
      else NodeSeq.Empty

    doRender(in) ++ tail
  }*/
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
