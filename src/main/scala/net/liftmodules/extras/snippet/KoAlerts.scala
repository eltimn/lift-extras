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

@deprecated("Moved to example app. Copy to your project if still using.", "0.7")
object KoAlerts extends KoAlerts

/**
  * A snippet for displaying notices to be used with the KoAlerts.js module.
  */
@deprecated("Moved to example app. Copy to your project if still using.", "0.7")
trait KoAlerts {
  import JsonDSL._

  lazy val koModule = KoModule("KoAlerts", "ko-alerts")

  def render(in: NodeSeq): NodeSeq = {

    val idsToListenFor: List[String] = S.attr("ids")
      .map(_.split(",").map(_.trim).toList)
      .openOr(Nil)

    def initData: JValue =
      ("titles" -> LiftExtras.titlesAsJValue) ~
      ("ids" -> idsToListenFor)

    val onLoad: JsCmd =
      koModule.init(initData) &
      LiftExtras.noticeConverter.vend.noticesToJsCmd

    S.appendJs(onLoad)

    in
  }
}
