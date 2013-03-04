package code
package config

import net.liftweb._
import common._
import http.S
import sitemap._
import sitemap.Loc._

object MenuGroups {
  val TopBarGroup = LocGroup("topbar")
}

/*
 * Wrapper for Menu locations
 */
case class MenuLoc(menu: Menu) {
  lazy val url: String = S.contextPath+menu.loc.calcDefaultHref
  lazy val fullUrl: String = S.hostAndPath+menu.loc.calcDefaultHref
}

object Site {
  import MenuGroups._

  // locations (menu entries)
  val home = MenuLoc(Menu.i("Home") / "index" >> TopBarGroup)

  private def menus = List(
    home.menu,
    Menu.i("About") / "about" >> TopBarGroup,
    Menu.i("Contact") / "contact" >> TopBarGroup,
    Menu.i("Throw") / "throw" >> Hidden,
    Menu.i("Error") / "error" >> Hidden,
    Menu.i("404") / "404" >> Hidden,
    Menu.i("MsgTest") / "msgtest" >> TopBarGroup
  )

  /*
   * Return a SiteMap needed for Lift
   */
  def siteMap: SiteMap = SiteMap(menus:_*)
}
