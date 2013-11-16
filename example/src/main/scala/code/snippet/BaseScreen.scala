package code
package snippet

import net.liftmodules.extras.Bootstrap3Screen

/*
 * Base all LiftScreens off this. Currently configured to use bootstrap.
 */
abstract class BaseScreen extends Bootstrap3Screen {
  override def defaultToAjax_? = true
}
