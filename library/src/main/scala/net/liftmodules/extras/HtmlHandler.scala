package net.liftmodules.extras

import scala.xml._

trait HtmlHandler {
  def noticeHtml(msg: String): NodeSeq
  def warningHtml(msg: String): NodeSeq
  def errorHtml(msg: String): NodeSeq
}

trait BootstrapHtmlHandler extends HtmlHandler {
  def noticeHtml(msg: String): NodeSeq = <div class="alert alert-info">{msg}</div>
  def warningHtml(msg: String): NodeSeq = <div class="alert alert-warning">{msg}</div>
  def errorHtml(msg: String): NodeSeq = <div class="alert alert-error">{msg}</div>
}

object BootstrapHtmlHandler extends BootstrapHtmlHandler
