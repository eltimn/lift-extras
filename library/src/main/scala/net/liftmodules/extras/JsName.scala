package net.liftmodules.extras


trait JsName {
  /**
    * Convert snippet/comet class name into a JavaScript name. It strips away the
    * packages up to and including snippet/comet, then prepends the js namespace.
    *
    * Ex: code.snippet.notice.MySnippet -> app.views.notice.MySnippet
    */
  def classNameToJsName: String = {
    val clsAsList = getClass.getName.split("\\.").toList
    val ix = clsAsList.indexWhere(s => Seq("snippet", "comet").contains(s))
    val modNameAsList =
      (if (ix >= 0 && clsAsList.length > ix+1) {
        clsAsList.slice(ix+1, clsAsList.length)
      }
      else {
        clsAsList
      }).map(_.replace("$", ""))

    (LiftExtras.jsNamespace.vend ++ modNameAsList).mkString(".")
  }
}
