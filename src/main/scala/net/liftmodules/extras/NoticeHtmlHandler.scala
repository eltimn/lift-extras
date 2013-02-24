package net.liftmodules.extras

import scala.xml._

trait NoticeHtmlHandler {
  def noticeHtml(msg: String): NodeSeq
  def warningHtml(msg: String): NodeSeq
  def errorHtml(msg: String): NodeSeq
}

trait DefaultNoticeHtmlHandler extends NoticeHtmlHandler {
  def noticeHtml(msg: String): NodeSeq = <div class="alert alert-info">{msg}</div>
  def warningHtml(msg: String): NodeSeq = <div class="alert alert-warning">{msg}</div>
  def errorHtml(msg: String): NodeSeq = <div class="alert alert-error">{msg}</div>
}

object DefaultNoticeHtmlHandler extends DefaultNoticeHtmlHandler
