package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http.js._
import JsCmds._
import JE._
import json._
import JsonDSL._

import NgJE._
import NgJsCmds._

class NgJsCmdsSpec extends WithSessionSpec {
  "NgJsCmds" should {
    "NgFactory" in {
      val facFunc = AnonFunc(JsReturn(JsObj(("x" -> Num(12)))))
      NgFactory("MyFactory", facFunc).toJsCmd should equal("""factory('MyFactory', function() {return {"x": 12}})""")
    }
    "NgService" in {
      val srvcFunc = AnonFunc(JsRaw("this.x = 12"))
      NgService("MyService", srvcFunc).toJsCmd should equal("""service('MyService', function() {this.x = 12;})""")
    }
    "NgConstant" in {
      val const = NgConstant("gretzky", Num(99))
      const.toJsCmd should equal("constant('gretzky', 99)")
    }
    "NgValue" in {
      val value = NgValue("beaupre", Num(33))
      value.toJsCmd should equal("value('beaupre', 33)")
    }
    "NgModule" in {
      val facFunc = AnonFunc(JsReturn(JsObj(("x" -> Num(12)))))
      val srvcFunc = AnonFunc(JsRaw("this.x = 12"))
      val mod =
        NgModule("MyApp", Seq("ui.bootstrap", "myco.modules")) ~>
          NgConstant("gretzky", Num(99)) ~>
          NgValue("beaupre", Num(33)) ~>
          NgFactory("MyFactory", facFunc) ~>
          NgService("MyService", srvcFunc)

      val mod2 = NgModule("MyApp2", Nil)
      mod.toJsCmd should equal("""angular.module('MyApp', ['ui.bootstrap', 'myco.modules']).constant('gretzky', 99).value('beaupre', 33).factory('MyFactory', function() {return {"x": 12}}).service('MyService', function() {this.x = 12;})""")
      mod2.toJsCmd should equal("angular.module('MyApp2', [])")
    }
  }
}
