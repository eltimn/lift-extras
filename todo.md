Required
========

* liftAjax
  * generate with Lift then copy and include in scripts manifest

* move JsModSnippet and KoSnippet defs to JsExtras and add arguments for elemenetId and modeulName

* JqNotifierNotices
* Rename BsNotices to JqBsNotices ???

Handling javascript
==================

* less

* use grunt to handle js and less processing
* Or use sbt plugins, but only for packaging
  * write snippet like bundles


* ditch bower - it's not ready yet - and just manually download deps.
  * keep bower and hardcode artefacts path in build script

* write snippet to load assets
  * would need a deps list (keep in the package.json file?)

* Assets snippet and versioning (is this necessary if using nodejs for less?)
  * sbt task that calls grunt dependsOn Package
  * use project version number
  * use buildInfo to generate a project.json file

* namepspace js modules

* load each snippet view model on each page instead of combining them

* how to run js tests

* put script tag at bottom of page

* sparky (check for organizing an app)

* grunt sublime build file

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
  * not all projects have a 'main' declared, so artefacts are not in known location
