package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http.js._
import JsCmds._
import JE._
import json._
import JsonDSL._

class SnippetExtrasSpec extends BaseSpec with SnippetExtras {
  "SnippetExtras" should {
    "create default notice html" in {
      val html = noticeHtml(Text("This is a test"))
      html should equal (<div class="alert alert-info">This is a test</div>)
    }
    "create default warning html" in {
      val html = warningHtml(Text("This is a test"))
      html should equal (<div class="alert alert-warning">This is a test</div>)
    }
    "create default error html" in {
      val html = errorHtml(Text("This is a test"))
      html should equal (<div class="alert alert-error">This is a test</div>)
    }
    "create custom notice html" in {
      LiftExtras.noticeHtmlHandler.doWith(CustomNoticeHtmlHandler) {
        val html = noticeHtml(Text("This is a test"))
        html should equal (<div class="notice">This is a test</div>)
      }
    }
    "create custom warning html" in {
      LiftExtras.noticeHtmlHandler.doWith(CustomNoticeHtmlHandler) {
        val html = warningHtml(Text("This is a test"))
        html should equal (<div class="warning">This is a test</div>)
      }
    }
    "create custom error html" in {
      LiftExtras.noticeHtmlHandler.doWith(CustomNoticeHtmlHandler) {
        val html = errorHtml(Text("This is a test"))
        html should equal (<div class="error">This is a test</div>)
      }
    }
    "convert Empty to warning html" in {
      val boxedHtml: Box[NodeSeq] = Empty
      val html: NodeSeq = boxedHtml
      html should equal (<div class="alert alert-warning">Unknown empty value</div>)
    }
    "convert Failure to error html" in {
      val boxedHtml: Box[NodeSeq] = Failure("Test failure")
      val html: NodeSeq = boxedHtml
      html should equal (<div class="alert alert-error">Test failure</div>)
    }
    "convert Empty to default JValue" in {
      val boxedJValue: Box[JValue] = Empty
      val jvalue: JValue = boxedJValue
      val expected: JValue =
        ("message" -> "Unknown empty value") ~
        ("priority" -> "warning") ~
        ("id" -> JNothing)

      jvalue should equal (expected)
    }
    "convert Failure to default JValue" in {
      val boxedJValue: Box[JValue] = Failure("Test failure")
      val jvalue: JValue = boxedJValue
      val expected: JValue =
        ("message" -> "Test failure") ~
        ("priority" -> "error") ~
        ("id" -> JNothing)

      jvalue should equal (expected)
    }
    "convert Empty to default JsCmd" in {
      val boxedJsCmd: Box[JsCmd] = Empty
      val jscmd: JsCmd = boxedJsCmd
      val expected: JsCmd =
        Call("$(document).trigger", Str("add-notices"), LiftNotice.warning("Unknown empty value").asJValue)

      jscmd should equal (expected)
    }
    "convert Failure to default JsCmd" in {
      val boxedJsCmd: Box[JsCmd] = Failure("Test failure")
      val jscmd: JsCmd = boxedJsCmd
      val expected: JsCmd =
        Call("$(document).trigger", Str("add-notices"), LiftNotice.error("Test failure").asJValue)

      jscmd should equal (expected)
    }
  }
}

object CustomNoticeHtmlHandler extends HtmlHandler {
  def noticeHtml(msg: NodeSeq): NodeSeq = <div class="notice">{msg}</div>
  def warningHtml(msg: NodeSeq): NodeSeq = <div class="warning">{msg}</div>
  def errorHtml(msg: NodeSeq): NodeSeq = <div class="error">{msg}</div>
}
