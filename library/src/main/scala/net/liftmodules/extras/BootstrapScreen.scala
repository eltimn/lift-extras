package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http._
import http.js._
import http.js.JsCmds._
import http.js.JE._
import util.CssSel
import util.Helpers._

/**
  * A screen with some bootstrap settings.
  */
trait BootstrapScreen extends LiftScreen {
  def cssErrorClass = "error" // BS3 = has-error

  override def cancelButton = super.cancelButton % ("class" -> "btn") % ("tabindex" -> "1")
  override def finishButton = super.finishButton % ("class" -> "btn btn-primary") % ("tabindex" -> "1")

  override protected def renderHtml(): NodeSeq = {
    S.appendJs(afterScreenLoad)
    super.renderHtml()
  }

  def displayOnly(fieldName: => String, html: => NodeSeq) =
    new Field {
      type ValueType = String
      override def name = fieldName
      override implicit def manifest = buildIt[String]
      override def default = ""
      override def toForm: Box[NodeSeq] = Full(html)
    }

  protected def afterScreenLoad: JsCmd = JsRaw("""
    |$(".form-alert").each(function() {
    |  $(this).closest("div.control-group").addClass("%s");
    |});
    """.format(cssErrorClass).stripMargin)
}
