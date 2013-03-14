package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http._
import http.js._
import JsCmds._
import JE._
import json._
import util.FieldError

/**
  * Class for modeling a Lift notice.
  */
case class LiftNotice(message: NodeSeq, priority: String, id: Option[String] = Empty) {
  def asJValue: JValue = LiftNotice.asJValue(this)
  def asJsCmd: JsCmd = LiftNotice.asJsCmd(this)
}

object LiftNotice {

  val Success = "success"

  private implicit def noticeTypeToString(notice: NoticeType.Value): String = notice.lowerCaseTitle

  def asJValue(notice: LiftNotice): JValue = LiftExtras.noticeConverter.vend.noticeAsJValue(notice)
  def asJsCmd(notice: LiftNotice): JsCmd = LiftExtras.noticeConverter.vend.noticeAsJsCmd(notice)
  def asJsCmd(notices: Seq[LiftNotice]): JsCmd = LiftExtras.noticeConverter.vend.noticesAsJsCmd(notices)

  def info(msg: String): LiftNotice = LiftNotice(Text(msg), NoticeType.Notice, Empty)
  def error(msg: String): LiftNotice = LiftNotice(Text(msg), NoticeType.Error, Empty)
  def warning(msg: String): LiftNotice = LiftNotice(Text(msg), NoticeType.Warning, Empty)
  def success(msg: String): LiftNotice = LiftNotice(Text(msg), Success, Empty)

  def info(msg: String, id: String): LiftNotice = LiftNotice(Text(msg), NoticeType.Notice, Some(id))
  def error(msg: String, id: String): LiftNotice = LiftNotice(Text(msg), NoticeType.Error, Some(id))
  def warning(msg: String, id: String): LiftNotice = LiftNotice(Text(msg), NoticeType.Warning, Some(id))
  def success(msg: String, id: String): LiftNotice = LiftNotice(Text(msg), Success, Some(id))

  def info(msg: NodeSeq): LiftNotice = LiftNotice(msg, NoticeType.Notice, Empty)
  def error(msg: NodeSeq): LiftNotice = LiftNotice(msg, NoticeType.Error, Empty)
  def warning(msg: NodeSeq): LiftNotice = LiftNotice(msg, NoticeType.Warning, Empty)
  def success(msg: NodeSeq): LiftNotice = LiftNotice(msg, Success, Empty)

  def info(msg: NodeSeq, id: String): LiftNotice = LiftNotice(msg, NoticeType.Notice, Some(id))
  def error(msg: NodeSeq, id: String): LiftNotice = LiftNotice(msg, NoticeType.Error, Some(id))
  def warning(msg: NodeSeq, id: String): LiftNotice = LiftNotice(msg, NoticeType.Warning, Some(id))
  def success(msg: NodeSeq, id: String): LiftNotice = LiftNotice(msg, Success, Some(id))

  def allNoticesAsJValue: JValue = JArray(S.getAllNotices
    .map { case (priority, msg, id) => LiftNotice(msg, priority, id).asJValue }
  )

  def fieldErrorsAsJValue(errs: List[FieldError]): JValue = JArray(errs
    .map { err => LiftNotice(err.msg, NoticeType.Error, err.field.uniqueFieldId).asJValue }
  )
}

trait LiftNoticeConverter {
  import JsonDSL._

  def noticeAsJValue(notice: LiftNotice): JValue =
    ("message" -> notice.message.toString) ~
    ("priority" -> notice.priority) ~
    ("id" -> notice.id)

  def noticeAsJsCmd(notice: LiftNotice): JsCmd = Call("$(document).trigger", Str("lift.notices.add"), notice.asJValue)
  def noticesAsJsCmd(notices: Seq[LiftNotice]): JsCmd = Call("$(document).trigger", Str("lift.notices.add"), JArray(notices.map(_.asJValue).toList))

  /**
    * Use this to set LiftRules.noticesToJsCmd
    */
  def noticesToJsCmd: JsCmd = Call("$(document).trigger", Str("lift.notices.set"), LiftNotice.allNoticesAsJValue)
}

object DefaultLiftNoticeConverter extends LiftNoticeConverter
