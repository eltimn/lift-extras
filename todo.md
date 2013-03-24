* update example to use bsFormAlerts.js & bsAlerts.js
* rename BsNotices to BsAlerts
* refactor JsModSnippet into a class that has methods

Launch
=======

* publish jquery plugins as separate repos
  * publish to bower
  * publish to jquery plugin repo

* write readme for lift-extras
* write blog posts
* push lift-tmpl.g8

* jquery.bsIdNotices.js & jquery.bsNotices.js
  * review bootstrap plugin docs
  * publish as a stand alone library.
    * create a package.json and publish to bower

* Tests


Open
====

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
