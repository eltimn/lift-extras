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
import util.{CssSel, Helpers}

object KoNotices extends KoNotices {

  def noticeAsJsCmd(notice: LiftNotice): JsCmd = Call("KoNotices.addNotices", notice.asJValue)

  def noticesToJsCmd: JsCmd = Call("KoNotices.setNotices", LiftNotice.allNoticesAsJValue)

  def init(): Unit = {
    LiftRules.noticesToJsCmd = noticesToJsCmd _

    /**
      * LiftScreen overwrites the class on form labels and bootstrap
      * requires the control-label class. So, we disable LiftScreen's
      * overwriting of the class.
      *
      * TODO: Is this still needed? Bootstrap's styles have changed since this was written.
      */
    LiftScreenRules.messageStyles.default.set({ nt: NoticeType.Value => nt match {
      case NoticeType.Notice => Null
      case NoticeType.Warning => Null
      case NoticeType.Error => Null
    }})
  }
}

trait KoNotices extends KoSnippet {

  override lazy val elementId = "ko-notices"
  override lazy val moduleName = "KoNotices"

  /**
    * Use this is if your page only has id notices
    */
  def idOnly =
    Script(OnLoad(
      JsModInit() &
      KoNotices.noticesToJsCmd
    ))

  override def doRender(in: NodeSeq): NodeSeq = {
    val showAll = Helpers.toBoolean(S.attr("showAll") or S.attr("showall"))
    val initData: JValue =
      ("showAll" -> showAll)

    val onLoad =
      KoInitBind(initData) &
      KoNotices.noticesToJsCmd &
      Call("""$("#%s").show""".format(elementId))

    S.appendJs(onLoad)

    <div id={elementId} style="display: none;">
      <div class="alert alert-error" data-bind="visible: errors().length > 0">
        <a href="javascript://" class="close" data-bind="click: clearErrors">&times;</a>
        <ul data-bind="foreach: errors">
          <li data-bind="text: message"></li>
        </ul>
      </div>
      <div class="alert alert-warning" data-bind="visible: warnings().length > 0">
        <a href="javascript://" class="close" data-bind="click: clearWarnings">&times;</a>
        <ul data-bind="foreach: warnings">
          <li data-bind="text: message"></li>
        </ul>
      </div>
      <div class="alert alert-info" data-bind="visible: infos().length > 0">
        <a href="javascript://" class="close" data-bind="click: clearInfos">&times;</a>
        <ul data-bind="foreach: infos">
          <li data-bind="text: message"></li>
        </ul>
      </div>
    </div>
  }

  def id(html: NodeSeq): NodeSeq = {
    S.attr("id") match {
      case Full(id) =>
        <div data-id-notice={id} class="notice-block">
          <span data-bind={"idnotice: idnotice_%s".format(id)}></span>
        </div>
      case _ => NodeSeq.Empty
    }
  }
}
