package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http.NoticeType
import http.js._
import JsCmds._
import JE._
import json._

/**
  * Class for modeling a Lift NoticeType.
  */
case class JsNotice(message: NodeSeq, noticeType: NoticeType.Value, id: Option[String] = Empty) {
  def asJValue: JValue = LiftExtras.noticeAsJValueFunc.vend(this)
  def asJsCmd: JsCmd = LiftExtras.noticeAsJsCmdFunc.vend(this)
}

object JsNotice {
  import JsonDSL._

  def info(msg: String): JsNotice = JsNotice(Text(msg), NoticeType.Notice, Empty)
  def error(msg: String): JsNotice = JsNotice(Text(msg), NoticeType.Error, Empty)
  def warning(msg: String): JsNotice = JsNotice(Text(msg), NoticeType.Warning, Empty)

  def info(msg: String, id: String): JsNotice = JsNotice(Text(msg), NoticeType.Notice, Some(id))
  def error(msg: String, id: String): JsNotice = JsNotice(Text(msg), NoticeType.Error, Some(id))
  def warning(msg: String, id: String): JsNotice = JsNotice(Text(msg), NoticeType.Warning, Some(id))

  def info(msg: NodeSeq): JsNotice = JsNotice(msg, NoticeType.Notice, Empty)
  def error(msg: NodeSeq): JsNotice = JsNotice(msg, NoticeType.Error, Empty)
  def warning(msg: NodeSeq): JsNotice = JsNotice(msg, NoticeType.Warning, Empty)

  def info(msg: NodeSeq, id: String): JsNotice = JsNotice(msg, NoticeType.Notice, Some(id))
  def error(msg: NodeSeq, id: String): JsNotice = JsNotice(msg, NoticeType.Error, Some(id))
  def warning(msg: NodeSeq, id: String): JsNotice = JsNotice(msg, NoticeType.Warning, Some(id))

  def asJValue(notice: JsNotice): JValue =
    ("message" -> notice.message.toString) ~
    ("notice_type" -> notice.noticeType.title) ~
    ("id" -> notice.id)

  def asJsCmd(notice: JsNotice): JsCmd = Call("LiftExtras.onNotice", notice.asJValue)
}
