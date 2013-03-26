package code.snippet
package notice

import code.model._

import scala.xml._

import net.liftweb._
import common._
import http.{S, SHtml, StatefulSnippet}
import http.js.JsCmd
import http.js.JsCmds._
import http.js.JE._
import json._
import util.Helpers._

import net.liftmodules.extras._

object FormsTest extends StatefulSnippet {
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

object FormsTestAjax extends SnippetHelper {

  import JsonDSL._

  val koModule = KoModule("App.views.notice.FormsTestAjax", "forms-test-ajax")

  def render(in: NodeSeq): NodeSeq = {
    val book = Book.createRecord.title("test title")

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

      book.validate match {
        case Nil =>
        case errs => S.error(errs)
      }

      /*LiftNotice.error("Created with case class").asJsCmd &
      LiftNotice.success(succ).asJsCmd &
      LiftNotice.success(succ+" (ajaxwarn)", "ajaxwarn").asJsCmd &
      LiftNotice.error(error+" (ajaxerr)", "ajaxerr").asJsCmd*/

      // Gets converted to JsCmd via implicit in SnippetHelper
      Seq(
        LiftNotice.success(succ),
        LiftNotice.info(<em>Another notice</em>)
      )
    }

    val opts: JValue = ("alertid" -> "text_id")
    val bindNoticeId = Call("$('#text_id_err').bsFormAlerts", opts)

    S.appendJs(koModule.init() & bindNoticeId)

    "name=error" #> SHtml.text(error, error = _) &
    "name=error_count" #> SHtml.selectElem[Int](0 to 10, Full(eCount))(eCount = _) &
    "name=warning" #> SHtml.text(warning, warning = _) &
    "name=warning_count" #> SHtml.selectElem[Int](0 to 10, Full(wCount))(wCount = _) &
    "name=info" #> SHtml.text(info, info = _) &
    "name=info_count" #> SHtml.selectElem[Int](0 to 10, Full(iCount))(iCount = _) &
    "name=title" #> book.title.toForm &
    "name=text" #> book.text.toForm &
    "name=sub" #> SHtml.hidden(process)
  } apply in
}
