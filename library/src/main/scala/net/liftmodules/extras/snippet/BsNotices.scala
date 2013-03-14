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
import util.{CssSel, Helpers}

object BsNotices extends Factory with BsNotices {
  /**
    * Config
    */
  val errorTitle = new FactoryMaker[Box[String]](Empty){}
  val warningTitle = new FactoryMaker[Box[String]](Empty){}
  val noticeTitle = new FactoryMaker[Box[String]](Empty){}
  val successTitle = new FactoryMaker[Box[String]](Empty){}


  def init(): Unit = {
    /**
      * LiftScreen overwrites the class on form labels and bootstrap
      * requires the control-label class. So, we disable LiftScreen's
      * overwriting of the class.
      */
    LiftScreenRules.messageStyles.default.set({ nt: NoticeType.Value => nt match {
      case NoticeType.Notice => Null
      case NoticeType.Warning => Null
      case NoticeType.Error => Null
    }})
  }
}

trait BsNotices extends JsModSnippet {
  override lazy val moduleName = "BsNotices"
  val elementId = LiftRules.noticesContainerId

  /**
    * Render notices
    */
  def doRender(html: NodeSeq): NodeSeq = {
    val showAll = Helpers.toBoolean(S.attr("showAll") or S.attr("showall"))
    val titles: JValue =
      ("error" -> BsNotices.errorTitle.vend.toOption) ~
      ("warning" -> BsNotices.warningTitle.vend.toOption) ~
      ("info" -> BsNotices.noticeTitle.vend.toOption) ~
      ("success" -> BsNotices.successTitle.vend.toOption)

    val initData: JValue =
      ("showAll" -> showAll) ~
      ("elementId" -> elementId) ~
      ("titles" -> titles)

    val onLoad: JsCmd =
      JsModInit(initData) &
      LiftExtras.noticeConverter.vend.noticesToJsCmd

    <div id={elementId}></div> ++ <tail>{Script(OnLoad(onLoad))}</tail>
  }

  /**
    * Render a single id's notices.
    */
  def id(html: NodeSeq): NodeSeq = {
    S.attr("id") match {
      case Full(id) =>
        <div data-id-notice={id} class="notice-block"></div>
      case _ => NodeSeq.Empty
    }
  }
}
