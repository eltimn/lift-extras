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

object BsNotices extends BsNotices

/**
  * A snippet for displaying notices to be used with the jquery.bsNotices.js plugin.
  */
trait BsNotices {
  def render(html: NodeSeq): NodeSeq = {
    // Needed for displaying notices when the page is loaded.
    S.appendJs(LiftExtras.noticeConverter.vend.noticesToJsCmd)

    val titlesJson: String = compact(JsonAST.render(LiftExtras.titlesAsJValue))

    <div data-notices="alerts" data-titles={titlesJson} data-ids="alerts, alerts2"></div>
  }
}
