package net.liftmodules.extras

import net.liftweb._
import common._
import http.{NoticeType, JsonContext, SHtml, S}
import http.js._
import JsCmds._
import JE._
import json._
import util.Helpers.tryo

/**
  * A snippet that uses a JavaScipt module. Provides function to initialize module.
  */
trait JsModSnippet {
  def moduleName: String

  /**
    * JsCmd to init a module
    */
  def JsModInit(params: JsExp*): JsCmd =
    Call("%s.init".format(moduleName), params:_*)
}
