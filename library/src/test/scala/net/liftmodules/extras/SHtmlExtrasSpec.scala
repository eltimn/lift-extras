package net.liftmodules.extras

import scala.xml._

import net.liftweb._
import common._
import http.js._
import JsCmds._
import JE._
import json._
import JsonDSL._

class SHtmlExtrasSpec extends WithSessionSpec {
  "SHtmlExtras" should {
    "create select element" in {
      val opts = (1, "One", "grp1") :: (2, "Two", "grp1") :: (3, "Three", "grp2") :: (4, "Four", "grp2") :: Nil
      def onSubmit(inst: Int): Unit = {}
      val select = SHtmlExtras.selectObj[Int](opts, Full(2), onSubmit _)
      // println(select)
      ((select \ "optgroup")(0) \ "@label").text should equal("grp1")
      ((select \ "optgroup")(1) \ "@label").text should equal("grp2")
      (select \\ "option").length should equal(5)
    }
  }
}
