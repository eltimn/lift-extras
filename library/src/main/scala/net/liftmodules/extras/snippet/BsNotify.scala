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

object BsNotify extends BsNotify

/**
  * A snippet for displaying notices to be used with the BsNotify.js module.
  */
trait BsNotify extends JsModSnippet {
  override lazy val moduleName = "BsNotify"

  override def doRender(in: NodeSeq): NodeSeq = {
    val initData: JValue =
      ("closable" -> true) ~
      ("transition" -> "fade") ~
      ("fadeOut" -> ("enabled" -> true) ~ ("delay" -> 5000))

    val onLoad: JsCmd =
      JsModInit(initData) &
      LiftExtras.noticeConverter.vend.noticesToJsCmd

    S.appendJs(onLoad)

    in
  }
}
