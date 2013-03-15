package net.liftmodules.extras

import net.liftweb._
import json._
import json.JsonDSL._

trait LiftNoticeSnippet {
  def elementId: String

  def titles: JValue =
    ("error" -> LiftExtras.errorTitle.vend.toOption) ~
    ("warning" -> LiftExtras.warningTitle.vend.toOption) ~
    ("info" -> LiftExtras.noticeTitle.vend.toOption) ~
    ("success" -> LiftExtras.successTitle.vend.toOption)

  def initData: JValue =
    ("elementId" -> elementId) ~
    ("titles" -> titles)
}
