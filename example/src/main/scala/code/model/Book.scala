package code.model

import scala.xml._

import net.liftweb._
import common._
import record._
import record.field._

class Book private () extends Record[Book] {
  def meta = Book

  object id extends IntField(this)
  object title extends StringField(this, 12) {
    override def displayName = "Title"
    override def helpAsHtml = Full(Text("The book's author"))

    override def validations =
      valMinLen(2, "Title must be at least 2 characters") _ ::
      valMaxLen(12, "Title must be 12 characters or less") _ ::
      super.validations
  }

  object text extends StringField(this, 12) {
    override def displayName = "Text"
    override def helpAsHtml = Full(Text("The book's text"))

    override def validations =
      valMinLen(2, "Text must be at least 2 characters") _ ::
      valMaxLen(12, "Text must be 12 characters or less") _ ::
      super.validations
  }

}

object Book extends Book with MetaRecord[Book]
