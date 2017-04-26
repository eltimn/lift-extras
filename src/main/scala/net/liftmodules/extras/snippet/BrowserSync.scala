package net.liftmodules.extras
package snippet

import scala.xml.NodeSeq
import java.nio.file.{ Files, Paths }
import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.util.Props
import net.liftweb.util.Helpers.tryo

/** Renders a script tag to fetch the browser-sync client.
  * Avoids having to use the default mechanism of injecting
  * the script tag via `document.writeTo`.
  *
  * Relies on the existence of a `package.json` file to
  * figure out the version.
  */
object BrowserSync extends SnippetHelper {
  private implicit val formats = DefaultFormats

  // Find the version used in `package.json`
  private def findVersion: Box[String] = {
    val filePath = Paths.get("package.json")
    if (Files.exists(filePath)) {
      for {
        txt <- tryo(new String(Files.readAllBytes(filePath)))
        json <- tryo(JsonParser.parse(txt))
        bsync <- Box((json \ "devDependencies" \ "browser-sync").extractOpt[String]) ?~ "Invalid package.json format"
      } yield {
        bsync
      }
    } else {
      Failure("package.json not found")
    }
  }

  def render(in: NodeSeq): NodeSeq = {
    if (Props.devMode) {
      (findVersion.map { ver =>
        <script async="" src={s"/browser-sync/browser-sync-client.js?v=${ver}"}></script>
      }): NodeSeq
    } else {
      NodeSeq.Empty
    }
  }
}
