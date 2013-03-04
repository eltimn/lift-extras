package code
package snippet

import code.lib._

import scala.xml.{NodeSeq, Text}
import java.util.Date

import net.liftweb.common._
import net.liftweb.util._
import Helpers._

import net.liftmodules.extras.SnippetExtras

object ErrorDivTest extends SnippetExtras {
  def render =
    (for {
      x <- Failure("test failure", Empty, Empty)
    } yield "* *" #> "Hola"
    ): CssSel
}

