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

object KnockoutExampleCls extends SnippetHelper with Loggable {
  import JsonDSL._

  implicit val formats = DefaultFormats

  val koClass = KoClass("App.views.knockout.KnockoutExampleCls", "window.koExample", "knockout-example-cls")

  def render(in: NodeSeq): NodeSeq = {
    /**
      * A test function that sends a success notice back to the client.
      */
    def sendSuccess(): JsCmd = LiftNotice.success(<em>You have success</em>).asJsCmd

    /**
      * A test function that sends an error notice back to the client.
      */
    def sendError(): JsCmd = LiftNotice.error(<em>You have error</em>).asJsCmd

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
        koClass.call("textInput", Str("")): JsCmd
      }
    }

    val initData: JValue =
      ("titles" -> LiftExtras.titlesAsJValue) ~
      ("fade" -> "3000")

    /**
      * Initialize the knockout view model, passing it the anonymous functions
      */
    val onload: JsCmd = koClass.init(
      JsExtras.AjaxCallbackAnonFunc(sendSuccess),
      JsExtras.AjaxCallbackAnonFunc(sendError),
      JsExtras.JsonCallbackAnonFunc(saveForm)
    ) & Call("""$("#notice-alerts").bsAlerts""", initData)

    S.appendJs(onload)

    in
  }
}
