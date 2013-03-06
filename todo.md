Required
========

* Gravatar

* liftAjax
  * make module

Handling javascript
==================
  * organization
  * optimization

  * checkout sbt-resource-management
  * load each snippet view model on each page instead of combining them

  * Assets snippet and versioning (is this necessary if using nodejs for less?)
    * sbt task that calls nodejs
    * sbt task that will rename css

  * JS dependencies management (Bower)
    * sbt-closure reads a bower manifest and fetchs dependencies (calls bower)

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
