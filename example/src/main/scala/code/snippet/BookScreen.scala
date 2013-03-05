package code.snippet

import code.model._

import net.liftweb._
import common._
import http._

object BookScreen extends BaseScreen with Loggable {
  override def defaultToAjax_? = true

  object bookVar extends ScreenVar(Book.createRecord)

  addFields(() => bookVar.is)

  def finish() {
    S.error("test error")
    S.notice("BookSaved: "+bookVar.is.toString)
  }
}
