Required
========

* deploy example to cloudbees
* jquery.bsIdNotices.js & jquery.bsNotices.js
  * publish to bower
* update and publish lift-tmpl.g8

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
  * response wrapper case class

Notes
=====

sbt-resource-management
  * can't filter less files to process
  * AWS settings shouldn't be required

bower
  * downloads entire project (this is needed for bootstrap)
  * not all projects have a 'main' declared, so artifacts are not in known location
