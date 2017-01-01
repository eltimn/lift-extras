package net.liftmodules.extras

import scala.xml._

trait HtmlHandler {
  def noticeHtml(msg: NodeSeq): NodeSeq
  def warningHtml(msg: NodeSeq): NodeSeq
  def errorHtml(msg: NodeSeq): NodeSeq
}

trait BootstrapHtmlHandler extends HtmlHandler {
  def noticeHtml(msg: NodeSeq): NodeSeq = <div class="alert alert-info">{msg}</div>
  def warningHtml(msg: NodeSeq): NodeSeq = <div class="alert alert-warning">{msg}</div>
  def errorHtml(msg: NodeSeq): NodeSeq = <div class="alert alert-danger">{msg}</div>
}

object BootstrapHtmlHandler extends BootstrapHtmlHandler
