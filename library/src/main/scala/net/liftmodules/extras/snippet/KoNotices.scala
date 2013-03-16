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

object KoNotices extends KoNotices

/**
  * A snippet for displaying notices to be used with the KoNotices.js module.
  */
trait KoNotices extends KoModSnippet {
  import JsonDSL._

  override lazy val moduleName = "KoNotices"

  def initData: JValue =
    ("elementId" -> elementId) ~
    ("titles" -> LiftExtras.titlesAsJValue)

  override def doRender(in: NodeSeq): NodeSeq = {
    val onLoad: JsCmd =
      KoInitBind(initData) &
      LiftExtras.noticeConverter.vend.noticesToJsCmd

    S.appendJs(onLoad)

    in
  }
}
