package code
package config

import code.model._

import net.liftweb._
import common._
import http.S
import sitemap._
import sitemap.Loc._

object MenuGroups {
  val TopBarGroup = LocGroup("topbar")
}

object Site {
  import MenuGroups._

  private def menus = List(
    Menu.i("Home") / "index" >> TopBarGroup,
    Menu.i("Notices") / "notices" >> TopBarGroup submenus(
      Menu.i("Forms Test") / "msgtest",
      Menu.i("Screen Test") / "book-edit"
    ),
    Menu.i("Knockout") / "knockout" >> TopBarGroup submenus(
      Menu.i("Knockout  Example") / "knockout-example",
      Menu.i("Knockout  Chat") / "chat-knockoutjs"
    ),
    Menu.i("About") / "about" >> TopBarGroup,
    Menu.i("Contact") / "contact" >> TopBarGroup,
    Menu.i("Throw") / "throw" >> Hidden,
    Menu.i("Error") / "error" >> Hidden,
    Menu.i("404") / "404" >> Hidden
  )

  /*
   * Return a SiteMap needed for Lift
   */
  def siteMap: SiteMap = SiteMap(menus:_*)
}
