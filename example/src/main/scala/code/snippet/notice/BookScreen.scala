package code.snippet
package notice

import code.model._

import net.liftweb._
import common._
import http._

object BookScreen extends BaseScreen with Loggable {

  object bookVar extends ScreenVar(Book.createRecord)

  addFields(() => bookVar.is)

  override def localSetup {
    Referer("/index")
  }

  def finish() {
    S.error(<em>test error</em>)
    S.notice("BookSaved: "+bookVar.is.toString)
  }
}
