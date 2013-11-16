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

  val errorTitle = new FactoryMaker[Box[NodeSeq]](Empty){}
  val warningTitle = new FactoryMaker[Box[NodeSeq]](Empty){}
  val noticeTitle = new FactoryMaker[Box[NodeSeq]](Empty){}
  val successTitle = new FactoryMaker[Box[NodeSeq]](Empty){}

  // HashedAssets/AssetLoader
  val artifactName = new FactoryMaker[String]("lift-app-0.1.0") {}
  val artifactPath = new FactoryMaker[Seq[String]](Seq("assets")) {}
  val artifactServer = new FactoryMaker[String]("") {}
  val mappingsUri = new FactoryMaker[String]("/assets.json") {}
  val assetServer = new Inject("") {}
  val jsSources = new Inject[Seq[String]](Seq("/vendor_scripts.txt", "/source_scripts.txt")) {}
  val cssSources = new Inject[Seq[String]](Seq("/vendor_styles.txt")) {}

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
    ("error" -> LiftExtras.errorTitle.vend.toOption.map(_.toString)) ~
    ("warning" -> LiftExtras.warningTitle.vend.toOption.map(_.toString)) ~
    ("info" -> LiftExtras.noticeTitle.vend.toOption.map(_.toString)) ~
    ("success" -> LiftExtras.successTitle.vend.toOption.map(_.toString))
}
