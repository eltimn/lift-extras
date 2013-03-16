package net.liftmodules.extras

import snippet._

import scala.xml._

import net.liftweb._
import common._
import http._
import http.js._
import JsCmds._
import JE._
import json._
import util.CssSel
import util.Helpers._

object LiftExtras extends Factory {
  import JsonDSL._

  /**
    * Config
    */
  val defaultEmptyMsg = new FactoryMaker[String]("Unknown empty value") {}
  val noticeHtmlHandler = new FactoryMaker[HtmlHandler](BootstrapHtmlHandler) {}
  val noticeConverter = new FactoryMaker[LiftNoticeConverter](DefaultLiftNoticeConverter) {}
  val parseJsonFunc = new FactoryMaker[(String, JValue => JsCmd) => JsCmd](JsExtras.defaultParseJsonFunc _) {}
  val jsNamespace = new FactoryMaker[Seq[String]](Seq("App", "views")) {}

  val errorTitle = new FactoryMaker[Box[String]](Empty){}
  val warningTitle = new FactoryMaker[Box[String]](Empty){}
  val noticeTitle = new FactoryMaker[Box[String]](Empty){}
  val successTitle = new FactoryMaker[Box[String]](Empty){}

  def init(): Unit = {
    LiftRules.noticesToJsCmd = noticeConverter.vend.noticesToJsCmd _

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

  def titlesAsJValue: JValue =
    ("error" -> LiftExtras.errorTitle.vend.toOption) ~
    ("warning" -> LiftExtras.warningTitle.vend.toOption) ~
    ("info" -> LiftExtras.noticeTitle.vend.toOption) ~
    ("success" -> LiftExtras.successTitle.vend.toOption)
}
