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

object KnockoutExampleMod extends SnippetHelper with Loggable {

  implicit val formats = DefaultFormats

  val koModule = KoModule("App.views.knockout.KnockoutExampleMod", "knockout-example-mod")

  def render(in: NodeSeq): NodeSeq = {
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
        koModule.call("textInput", Str("")): JsCmd
      }
    }

    /**
      * A test function that sends a success notice back to the client.
      */
    def sendSuccess(): JsCmd = LiftNotice.success("You have success").asJsCmd

    /**
      * Initialize the knockout view model, passing it the anonymous functions
      */
    val onload: JsCmd = koModule.init(
      JsExtras.JsonCallbackAnonFunc(saveForm),
      JsExtras.AjaxCallbackAnonFunc(sendSuccess)
    )

    S.appendJs(onload)

    in
  }
}
