package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http.{Factory, RenderOut}
import http.js._
import JsCmds._
import JE._
import json._
import util.CssSel
import util.Helpers._

object LiftExtras extends LiftExtras with Factory {
  val noticeHtmlHandler = new FactoryMaker[NoticeHtmlHandler](DefaultNoticeHtmlHandler) {}
  val noticeAsJValueFunc = new FactoryMaker[JsNotice => JValue](JsNotice.asJValue _) {}
  val noticeAsJsCmdFunc = new FactoryMaker[JsNotice => JsCmd](JsNotice.asJsCmd _) {}

  val parseJValueFunc = new FactoryMaker[(String, JValue => JsCmd) => JsCmd](SHtmlExtras.parseJValue _) {}
}

trait LiftExtras extends SHtmlExtras with NoticeExtras with MiscExtras
