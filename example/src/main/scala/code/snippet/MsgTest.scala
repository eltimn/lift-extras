package code
package snippet

import net.liftweb._
import common._
import http.{S, SHtml, StatefulSnippet}
import http.js.JsCmd
import http.js.JsCmds._
import json._
import util.Helpers._

import net.liftmodules.extras._

object MsgTest extends StatefulSnippet {
  def dispatch = { case _ => render }

  private var error = "This is an error"
  private var eCount = 0
  private var warning = "This is a warning"
  private var wCount = 0
  private var info = "This is some info"
  private var iCount = 0

  def render = {
    "name=error" #> SHtml.text(error, error = _) &
    "name=error_count" #> SHtml.selectElem[Int](0 to 10, Full(eCount))(eCount = _) &
    "name=warning" #> SHtml.text(warning, warning = _) &
    "name=warning_count" #> SHtml.selectElem[Int](0 to 10, Full(wCount))(wCount = _) &
    "name=info" #> SHtml.text(info, info = _) &
    "name=info_count" #> SHtml.selectElem[Int](0 to 10, Full(iCount))(iCount = _) &
    "type=submit" #> SHtml.submit("Submit", () => {
      for (i <- 0 until eCount)
        S.error(error)
      for (i <- 0 until wCount)
        S.warning(warning)
      for (i <- 0 until iCount)
        S.notice(info)

      S.error("err", "Example field error (err)")
      S.error("err", "Example field error2 (err)")
      S.warning("err", "Example field warning (err)")
      S.notice("err", "Example field notice (err)")

      S.redirectTo(S.uri)
    })
  }
}

object MsgTestAjax extends KoSnippet {

  val moduleName = "MsgTestAjax"
  val elementId = "msg-test-ajax"

  val onLoad: JsCmd = {
    KoInitBind()
  }

  def render = {
    var error = "This is an error"
    var eCount = 0
    var warning = "This is a warning"
    var wCount = 0
    var info = "This is some info"
    var iCount = 0
    var succ = "Success!"

    def process(): JsCmd = {
      for (i <- 0 until eCount) {
        S.error(error)
        S.error("ajaxerr", error+" (ajaxerr)")
        S.error("ajaxwarn", error+" (ajaxwarn)")
      }
      for (i <- 0 until wCount) {
        S.warning(warning)
        S.warning("ajaxerr", warning+" (ajaxerr)")
        S.warning("ajaxwarn", warning+" (ajaxwarn)")
      }
      for (i <- 0 until iCount) {
        S.notice(info)
        S.notice("ajaxerr", info+" (ajaxerr)")
        S.notice("ajaxwarn", info+" (ajaxwarn)")
      }

      Thread.sleep(200) // so we can see the spinner

      /*LiftNotice.error("Created with case class").asJsCmd &
      LiftNotice.success(succ).asJsCmd &
      LiftNotice.success(succ+" (ajaxwarn)", "ajaxwarn").asJsCmd &
      LiftNotice.error(error+" (ajaxerr)", "ajaxerr").asJsCmd*/
      LiftNotice.success(succ+" (ajaxinfo)").asJsCmd
    }

    "name=error" #> SHtml.text(error, error = _) &
    "name=error_count" #> SHtml.selectElem[Int](0 to 10, Full(eCount))(eCount = _) &
    "name=warning" #> SHtml.text(warning, warning = _) &
    "name=warning_count" #> SHtml.selectElem[Int](0 to 10, Full(wCount))(wCount = _) &
    "name=info" #> SHtml.text(info, info = _) &
    "name=info_count" #> SHtml.selectElem[Int](0 to 10, Full(iCount))(iCount = _) &
    "name=sub" #> SHtml.hidden(process) &
    "#msg-test-ajax-onload" #> Script(OnLoad(onLoad))
  }
}
