package code.snippet
package notice

import code.lib._

import scala.xml.{NodeSeq, Text}
import java.util.Date

import net.liftweb.common._
import net.liftweb.util._
import Helpers._

import net.liftmodules.extras.SnippetHelper

object ErrorDivTest extends SnippetHelper {
  def render =
    (for {
      x <- Failure("test failure", Empty, Empty)
    } yield "* *" #> "Etiam porta sem malesuada magna mollis euismod."
    ): CssSel
}

