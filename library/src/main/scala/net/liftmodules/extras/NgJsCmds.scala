package net.liftmodules.extras

import net.liftweb._
import common._
import http._
import http.js._
import JsCmds._
import JE._
import json._

import JsExtras.IIFE

/**
  * Contains Scala JsExps for Angular behaviors.
  *
  * These functions are meant to be combined using the ~> operator. For
  * example:
  *
  *   <pre>NgModule("LiftServer", Nil) ~> NgFactory(name, AnonFunc(...))</pre>
  *
  */
object NgJE {
  case class NgModule(name: String, dependencies: Seq[String]) extends JsExp {
    def toJsCmd = {
      "angular.module('%s', [%s])".format(name, dependencies.map(s => "'%s'".format(s)).mkString(", "))
    }
  }

  case class NgFactory(name: String, func: AnonFunc) extends JsExp with JsMember {
    def toJsCmd = {
      "factory('%s', %s)".format(name, func.toJsCmd)
    }
  }

  case class NgService(name: String, func: AnonFunc) extends JsExp with JsMember {
    def toJsCmd = {
      "service('%s', %s)".format(name, func.toJsCmd)
    }
  }

  case class NgConstant(name: String, value: JsExp) extends JsExp with JsMember {
    def toJsCmd = {
      "constant('%s', %s)".format(name, value.toJsCmd)
    }
  }

  case class NgValue(name: String, value: JsExp) extends JsExp with JsMember {
    def toJsCmd = {
      "value('%s', %s)".format(name, value.toJsCmd)
    }
  }
}

object NgJsCmds {

  private implicit def boxedJValueToJsExp(in: Box[JValue]): JsExp = in.map(jv => new JsExp {
    def toJsCmd = compact(render(jv))
  }).openOr(JsNull)

  /**
    * Call `&#36;scope.&#36;apply` on the passed elementId
    */
  case class NgApply(elementId: String, cmd: JsCmd) extends JsCmd {
    def toJsCmd = WithScopeVar(elementId, Call("scope.$apply", AnonFunc(cmd))).toJsCmd
  }

  /**
    * Call `&#36;scope.&#36;broadcast` on the passed elementId
    */
  case class NgBroadcast(elementId: String, event: String, json: Box[JValue] = Empty) extends JsCmd {
    def toJsCmd = WithScopeVar(elementId, Call("scope.$broadcast", event, json)).toJsCmd
  }

  /**
    * Set a local variable to the scope of an elementId and execute the cmd. All of which is enclosed in an IIFE.
    */
  case class WithScopeVar(elementId: String, cmd: JsCmd) extends JsCmd {
    def toJsCmd = IIFE(
      JsCrVar("scope", Call("angular.element('#%s').scope".format(elementId))) &
      cmd
    ).toJsCmd
  }
}
