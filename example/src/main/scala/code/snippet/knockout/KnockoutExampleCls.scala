package code.snippet
package knockout

import scala.xml._

import net.liftweb._
import common._
import http._
import http.js._
import http.js.JsCmds._
import http.js.JE._
import json._
import util.Helpers._

import net.liftmodules.extras._

object KnockoutExampleCls extends SnippetExtras with KoClsSnippet with Loggable {
  import JsonDSL._

  implicit val formats = DefaultFormats

  def jsVarName = "window.koExample"

  def doRender(in: NodeSeq): NodeSeq = {
    /**
      * The function to call when submitting the form.
      */
    def saveForm(json: JValue): JsCmd = {
      for {
        msg <- tryo((json \ "textInput").extract[String])
      } yield {
        val logMsg = "textInput from client: "+msg
        logger.info(logMsg)
        S.notice(logMsg)
        CallJsVar("textInput", Str("")): JsCmd
      }
    }

    /**
      * A test function that sends a success notice back to the client.
      */
    def sendSuccess(): JsCmd = LiftNotice.success(<em>You have success</em>).asJsCmd

    val initData: JValue =
      ("titles" -> LiftExtras.titlesAsJValue)

    /**
      * Initialize the knockout view model, passing it the anonymous functions
      */
    val onload: JsCmd = KoInitBind(
      JsExtras.JsonCallbackAnonFunc(saveForm),
      JsExtras.AjaxCallbackAnonFunc(sendSuccess)
    ) & Call("""$("#notice-alerts").bsNotices""", initData)

    S.appendJs(onload)

    in
  }
}
