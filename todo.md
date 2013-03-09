Required
========

* use watch with grunt to auto build js/less

* how to run js tests
  * use grunt-jasmine

* Create a base class for using with BsNotices and JqNotifierNotices
* JqNotifierNotices
* Rename BsNotices to JqBsNotices ???
* separate group from id notices ???

* Create top level global namespace to house all lift stuff. Add these as extensions to that.

* move BsNotices.js to library
  * create a package.json and publish to bower

Organizing JavaScript Code
==========================

* use a single global variable for all of our code
* namespace using object literal notation with auto extend function
* write page classes as modules
  * wrap in
* access other utils using local vars - Dependency declaration pattern

Next
====

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
