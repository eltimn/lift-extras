package net.liftmodules.extras
package snippet

import scala.xml._

import net.liftweb._
import common._
import http._
import http.js._
import http.js.JsCmds._
import http.js.JE._
import json._
import json.JsonDSL._
import util.{CssSel, Helpers}

trait BsNotices extends JsModSnippet with LiftNoticeSnippet {
  override lazy val moduleName = "BsNotices"
  val elementId = "bs-notices"

  /**
    * Render notices
    */
  def doRender(html: NodeSeq): NodeSeq = {
    val onLoad: JsCmd =
      JsModInit(initData) &
      LiftExtras.noticeConverter.vend.noticesToJsCmd

    S.appendJs(onLoad)

    <div id={elementId}></div>
  }
}
