package net.liftmodules.extras

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

object SnippetHelper extends SnippetHelper

trait SnippetHelper {

  def emptyMsg = LiftExtras.defaultEmptyMsg.vend

  def noticeHtml(msg: NodeSeq): NodeSeq = LiftExtras.noticeHtmlHandler.vend.noticeHtml(msg)
  def warningHtml(msg: NodeSeq): NodeSeq = LiftExtras.noticeHtmlHandler.vend.warningHtml(msg)
  def errorHtml(msg: NodeSeq): NodeSeq = LiftExtras.noticeHtmlHandler.vend.errorHtml(msg)

  /**
    * Allows for the following to be used when building snippets that return NodeSeq.
    *
    * Usage:
    *
    *   for {
    *     user <- User.currentUser ?~ "You must be logged in to edit your profile."
    *   } yield ({
    *   ...
    *   }): NodeSeq
    */
  implicit protected def boxNodeSeqToNodeSeq(in: Box[NodeSeq]): NodeSeq = in match {
    case Full(ns) => ns
    case Failure(msg, _, _) => errorHtml(Text(msg))
    case Empty => warningHtml(Text(emptyMsg))
  }

  /**
    * Allows for the following to be used when building snippets that return CssSel.
    *
    * Usage:
    *
    *   for {
    *     user <- User.currentUser ?~ "You must be logged in to edit your profile."
    *   } yield ({
    *   ...
    *   }): CssSel
    */
  implicit protected def boxCssSelToCssSel(in: Box[CssSel]): CssSel = in match {
    case Full(csssel) => csssel
    case Failure(msg, _, _) => "*" #> errorHtml(Text(msg))
    case Empty => "*" #> warningHtml(Text(emptyMsg))
  }

  /**
    * Allows for the following to be used when building Comet functions that return RenderOut. Usage:
    *
    *   for {
    *     user <- User.currentUser ?~ "You must be logged in to edit your profile."
    *   } yield ({
    *   ...
    *   }): RenderOut
    */
  implicit protected def boxRenderOutToRenderOut(in: Box[RenderOut]): RenderOut = in match {
    case Full(ro) => ro
    case Failure(msg, _, _) => new RenderOut(errorHtml(Text(msg)))
    case Empty => new RenderOut(warningHtml(Text(emptyMsg)))
  }

  /**
    * Allows for the following to be used when building functions that return JsCmd. Usage:
    *
    *   for {
    *     user <- User.currentUser ?~ "You must be logged in to edit your profile."
    *   } yield ({
    *   ...
    *   }): JsCmd
    */
  implicit protected def boxJsCmdToJsCmd(in: Box[JsCmd]): JsCmd = in match {
    case Full(jscmd) => jscmd
    case Failure(msg, _, _) => LiftNotice.error(Text(msg)).asJsCmd
    case Empty => LiftNotice.warning(Text(emptyMsg)).asJsCmd
  }

  /**
    * Allows for the following to be used when building functions that return JValue. Usage:
    *
    *   for {
    *     user <- User.currentUser ?~ "You must be logged in to edit your profile."
    *   } yield ({
    *   ...
    *   }): JValue
    */
  implicit protected def boxJValueToJValue(in: Box[JValue]): JValue = in match {
    case Full(jv) => jv
    case Failure(msg, _, _) => LiftNotice.error(Text(msg)).asJValue
    case Empty => LiftNotice.warning(Text(emptyMsg)).asJValue
  }

  /**
    * Converts a Seq of LiftNotice to JsCmd
    */
  implicit protected def liftNoticesToJsCmd(in: Seq[LiftNotice]): JsCmd =
    LiftExtras.noticeConverter.vend.noticesAsJsCmd(in)

  /**
    * Converts a single LiftNotice to JsCmd
    */
  implicit protected def liftNoticeToJsCmd(in: LiftNotice): JsCmd =
    LiftExtras.noticeConverter.vend.noticesAsJsCmd(Seq(in))

  /**
    * Allows easier ways to add attributes to Elem
    */
  class RichElem(elem: Elem) {
    def %(attrs: Map[String, String]) = attrs.foldLeft(elem) {
      case (el, (key, value)) => el % new UnprefixedAttribute(key, value, Null)
    }
    def %(attrs: List[(String, String)]) = attrs.foldLeft(elem) {
      case (el, (key, value)) => el % new UnprefixedAttribute(key, value, Null)
    }
  }
  implicit def richElem(elem: Elem) = new RichElem(elem)

  /**
    * For adding checked, selected, and disabled attributes to Elem.
    */
  def checked(in: Boolean) = if (in) new UnprefixedAttribute("checked", "checked", Null) else Null
  def selected(in: Boolean) = if (in) new UnprefixedAttribute("selected", "selected", Null) else Null
  def disabled(in: Boolean) = if (in) new UnprefixedAttribute("disabled", "disabled", Null) else Null
  def autofocus(in: Boolean) = if (in) new UnprefixedAttribute("autofocus", "true", Null) else Null
}
