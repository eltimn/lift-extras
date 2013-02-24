package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http.js._
import json._
import util.Helpers._

object MiscExtras extends MiscExtras

trait MiscExtras {

  /**
    * Call a JavaScript function preceded by the new operator.
    */
  case class CallNew(function: String, params: JsExp*) extends JsExp {
    def toJsCmd = "new " + function + "(" + params.map(_.toJsCmd).mkString(",") + ")"
  }

  /**
    * For adding checked, selected, and disabled attributes to Elem.
    */
  def checked(in: Boolean) = if (in) new UnprefixedAttribute("checked", "checked", Null) else Null
  def selected(in: Boolean) = if (in) new UnprefixedAttribute("selected", "selected", Null) else Null
  def disabled(in: Boolean) = if (in) new UnprefixedAttribute("disabled", "disabled", Null) else Null
}
