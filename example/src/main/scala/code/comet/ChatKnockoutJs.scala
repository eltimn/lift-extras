package code
package comet

import scala.xml._

import net.liftweb._
import common._
import http._
import js._
import js.JE._
import js.JsCmds._
import json._
import json.JsonDSL._
import util._
import Helpers._

import net.liftmodules.extras.{JsExtras, KoComet, SnippetExtras}


/**
 * The screen real estate on the browser will be represented
 * by this component.  When the component changes on the server
 * the changes are automatically reflected in the browser.
 */
class ChatKnockOutJs extends CometActor with CometListener with SnippetExtras with KoComet {
  // val elementId = "chat-messages"
  // val moduleName = "ChatMessages"

  case class NewMessageKo(message: String) extends JsCmd {
    implicit val formats = DefaultFormats.lossless
    val json: JValue = ("message" -> message)
    override val toJsCmd =
      // JE.Call("$(document).trigger", JE.Str("new-ko-chat"), json).toJsCmd
      JE.Call("%s.addMessage".format(moduleName), json).toJsCmd
  }

  implicit def formats = DefaultFormats

  private var msgs: Vector[String] = Vector() // private state

  /**
   * When the component is instantiated, register as
   * a listener with the ChatServer
   */
  def registerWith = ChatServer

  /**
   * The CometActor is an Actor, so it processes messages.
   * In this case, we're listening for Vector[String],
   * and when we get one, update our private state
   * and reRender() the component.  reRender() will
   * cause changes to be sent to the browser.
   */
  override def lowPriority = {
    case v: Vector[String] =>
      msgs = v
      partialUpdate(NewMessageKo(v.last))
  }

  /**
    * The function to call when submitting the form.
    */
  def saveForm(json: JValue): JsCmd = {
    for {
      msg <- tryo((json \ "message").extract[String])
    } yield {
      ChatServer ! msg
      Call("%s.newMessage".format(moduleName), Str("")): JsCmd
    }
  }

  /**
    * Initialize the knockout view model, passing it an anonymous function that calls saveForm
    */
  val onload: JsCmd = KoInitBind(JsExtras.JsonCallbackAnonFunc(saveForm _))

  /**
    * Provide the doRender function for KoComet
    */
  def doRender(in: NodeSeq): NodeSeq = <tail>{Script(OnLoad(onload))}</tail>

  /*override def render =
    (for {
      x <- Failure("test failure", Empty, Empty)
    } yield new RenderOut(<p>This won't be seen</p>)
    ): RenderOut*/
}
