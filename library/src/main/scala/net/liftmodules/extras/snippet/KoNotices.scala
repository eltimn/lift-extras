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
import util.Helpers.toBoolean

object KoNotices extends KoNotices

/**
  * A snippet for displaying notices to be used with the KoNotices.js module.
  */
trait KoNotices extends KoModSnippet {
  import JsonDSL._

  override lazy val moduleName = "KoNotices"

  override def doRender(in: NodeSeq): NodeSeq = {

    val idsToListenFor: List[String] = S.attr("ids")
      .map(_.split(",").map(_.trim).toList)
      .openOr(Nil)

    def initData: JValue =
      ("titles" -> LiftExtras.titlesAsJValue) ~
      ("ids" -> idsToListenFor)

    val onLoad: JsCmd =
      KoInitBind(initData) &
      LiftExtras.noticeConverter.vend.noticesToJsCmd

    S.appendJs(onLoad)

    in
  }
}
