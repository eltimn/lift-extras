package net.liftmodules
package extras

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util._
import net.liftweb.util.Helpers._

trait BaseSpec extends AnyWordSpec with Matchers

trait WithSessionSpec extends BaseSpec {
  def session = new LiftSession("", randomString(20), Empty)

  override protected def withFixture(test: NoArgTest) = {
    S.initIfUninitted(session) { test() }
  }
}
