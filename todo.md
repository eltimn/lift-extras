Required
========

* move KoAlerts and BsNotify into library.

* write readme for lift-extras
  * link to blog post about SnippetExtras
  * download links for JavaScript and html for KoAlerts and BsNotify

* write blog posts
  * Writing JavaScript apps with Lift
    * Intro
      * best practices
        * limit number of scripts loaded
        * limit inline javascript
        * combine into one file (js and css)
        * cache in production with a far future expires (only every download once)
        * requires renaming your file every time you deploy. query string is not enough.
    * Organization
      * a single global object
      * namespaced classes and modules
      * one snippet one js class
      *
      * module vs class
    * Callback functions
    * Grunt


* push lift-tmpl.g8

* jquery.bsIdNotices.js & jquery.bsNotices.js
  * publish to bower

Open
====

* Tests

* use requirejs by default (see if ruby is needed)

* AngularJS demo
* Flight demo


Next
====

* RestExtras
  * boxJValueToJValue
  * response wrapper case class = see what Foursquare does

Possibly
========

* Ko Helpers - not sure what more we need
  * possibly some custom bindings for use w/ Lift

* User ClearClearable instead of calling clearNotices


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


Organizing JavaScript Code
==========================
* Use mixin style
  * http://javascriptweblog.wordpress.com/2011/05/31/a-fresh-look-at-javascript-mixins/
  * http://jsfiddle.net/eltimn/khrF6/1/
  * https://speakerdeck.com/anguscroll/how-we-learned-to-stop-worrying-and-love-javascript
  * https://gist.github.com/angus-c/2864853


the rest of this needs review:

* use a single global variable for all of our code
* namespace using object literal notation with auto extend function - rename extend to namespace or something else. extend may become a reserved word
* write page classes as modules
  * wrap in
* access other utils using local vars - Dependency declaration pattern



writing knockout classes
* Use mixin style described here:
https://speakerdeck.com/anguscroll/how-we-learned-to-stop-worrying-and-love-javascript
  * mixin on fiddle
  * withAdvice
