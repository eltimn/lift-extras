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

object BsAlerts extends BsAlerts

/**
  * A snippet for displaying notices to be used with the jquery.bsNotices.js plugin.
  */
trait BsAlerts {
  def render(html: NodeSeq): NodeSeq = {
    // Needed for displaying notices when the page is loaded.
    S.appendJs(LiftExtras.noticeConverter.vend.noticesToJsCmd)
    val ids = S.attr("ids").openOr("")
    val titles: String = compact(JsonAST.render(LiftExtras.titlesAsJValue))
    val fade: String = S.attr("fade").openOr("0")

    <div data-alerts="alerts" data-titles={titles} data-ids={ids} fade={fade}></div>
  }
}
