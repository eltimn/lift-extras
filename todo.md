Required
========


* Create a base class for using with BsNotices and JqNotifierNotices
* JqNotifierNotices
* Rename BsNotices to JqBsNotices ???

* put script tag at bottom of page

* namepspace js modules

* load each snippet view model on each page instead of combining them

* how to run js tests

* sparky (check for organizing an app)

* grunt sublime build file

* move JsModSnippet and KoSnippet defs to JsExtras and add arguments for elementId and modelName ???
* Or, add auto script tag inclusion

Next
====

* optionally include script tag to include view model for that snippet.

* snippets
  * Menus.item - improve

* Tests

* RestExtras
  * boxJValueToJValue
  * response wrapper case class = see what Foursquare does

Possibly
========

* tail for JsCmds
* Write BsNotices as a jQuery plugin ???

* Ko Helpers - not sure what more we need
  * possibly some custom bindings for use w/ Lift


Notes
=====

sbt-resource-management
  * can't filter less files to process
  * AWS settings shouldn't be required

bower
  * downloads entire project (this is needed for bootstrap)
  * not all projects have a 'main' declared, so artifacts are not in known location

liftAjax
  * generate with Lift then copy and include in build
