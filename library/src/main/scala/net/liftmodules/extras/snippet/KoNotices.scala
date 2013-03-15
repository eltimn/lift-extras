package net.liftmodules.extras
package snippet

import scala.xml.{NodeSeq, Null, Text}

import net.liftweb._
import common._
import http._
import http.js._
import http.js.JsCmds._
import http.js.JE._
import json._
import json.JsonDSL._
import util.{CssSel, Helpers}

trait KoNotices extends KoModSnippet with LiftNoticeSnippet {
  override lazy val moduleName = "KoNotices"

  override def doRender(in: NodeSeq): NodeSeq = {
    val onLoad: JsCmd =
      KoInitBind(initData) &
      LiftExtras.noticeConverter.vend.noticesToJsCmd

    S.appendJs(onLoad)

    in
  }
}
