Extras Lift Module
==================

[![Build Status](https://travis-ci.org/eltimn/lift-extras.svg?branch=master)](https://travis-ci.org/eltimn/lift-extras)

Some extras for developing Lift webapps.

# Installation

Releases uses the Lift "edition" in the name. For example, if you use any of 2.5-SNAPSHOT, 2.5-RC4, or 2.5 the Lift edition is 2.5.

**Note:** some of the older versions published had a '-' in the edition instead of a '.'. I.e *extras_3-0* instead of *extras_3.0*.

For *Lift 3.0* (Scala 2.11, 2.12):

    libraryDependencies += "net.liftmodules" %% "extras_3.0" % "0.7"

For *Lift 2.5.x* (Scala 2.9 and 2.10):

    libraryDependencies += "net.liftmodules" %% "extras_2.5" % "0.3"

For *Lift 2.5.x* (Scala 2.10):

    libraryDependencies += "net.liftmodules" %% "extras_2.5" % "0.4-SNAPSHOT"

For *Lift <= 2.6-M3* (Scala 2.9 and Scala 2.10):

    libraryDependencies += "net.liftmodules" %% "extras_2.6" % "0.3"

For *Lift => 2.6-M4* (Scala 2.10, 2.11):

    libraryDependencies += "net.liftmodules" %% "extras_2.6" % "0.4"


# Documentation

## LiftNotice

A case class that models a Lift notice and a companion object that provides:

* conversion functions to JValue and JsCmd
* a function for overriding LiftRules.noticesToJsCmd
* a `success` notice type

The `noticesToJsCmd` function returns a JsCmd that sends jQuery events to the browser. This makes it easy to write custom JavaScript that listens for the events and displays the notices.

The main motivation for writing this was to have a way to display alerts directly from JavaScript and not have to call the server. And I didn't want to have to duplicate code. H/T to @fmpwizard and @shadowfiend for the idea to use events.

See the documentation for the two jQuery plugins for more details on the events.

### [jQuery-bsAlerts](http://eltimn.github.com/jquery-bs-alerts/index.html "jQuery-bsAlerts")

A jQuery plugin for displaying Bootstrap alerts via jQuery events. See also the companion BsAlerts snippet.

### [jQuery-bsFormAlerts](http://eltimn.github.com/jquery-bs-formalerts/index.html "jQuery-bsFormAlerts")

A jQuery plugin for displaying Bootstrap form alerts via jQuery events.

### KoAlerts

A snippet that uses a Knockout.js `View Model` to display notices. Does not rely on Bootstrap. Allows for any html that complies with the `View Model`. See [KoAlerts.scala](https://github.com/eltimn/lift-extras/blob/master/library/src/main/scala/net/liftmodules/extras/snippet/KoAlerts.scala) and [alerts.html](https://github.com/eltimn/lift-extras/blob/master/example/src/main/webapp/templates-hidden/alerts.html) for details.

[Download JavaScript](https://raw.github.com/eltimn/lift-extras/master/example/src/main/javascript/KoAlerts.js)

### BsNotify

This is a JavaScript module that is a wrapper around [bootstrap-notify](http://nijikokun.github.com/bootstrap-notify/ "bootstrap-notify") that provides an interface to the events mentioned above. Requires added a div tag to your pages. See [base-wrap.html](https://github.com/eltimn/lift-extras/blob/master/example/src/main/webapp/templates-hidden/base-wrap.html#L41) for an example.

[Download JavaScript](https://raw.github.com/eltimn/lift-extras/master/example/src/main/javascript/BsNotify.js)

## BsMenu

A snippet for rendering a Bootstrap menu using Groups.

## BootstrapScreen

A base LiftScreen trait that works with Bootstrap. Requires accompanying wizard-all.html file.

## Gravatar

An object for displaying Gravatar images.

## JsExtras

A few classes for passing callback functions to client-side JavaScript. See [JavaScript Apps With Lift: Callback Functions](http://www.eltimn.com/blog/004-javascript-apps-with-lift-callback-functions) for more information.

### AjaxCallbackAnonFunc

Creates an anonymous JavaScript function that calls a server side function, then runs the server-side functions return JsCmd on the client.

### JsonCallbackAnonFunc

Creates an anonymous JavaScript function that takes JSON as a parameter and calls a server side function, then runs the server-side functions return JsCmd on the client.

## SnippetHelper

A trait with a series of implicit functions that convert a Box(ed) value (NodeSeq, JsCmd, CssSel) to it's inner value if Full and will display an error otherwise. Uses LiftNotice.

It allows you to write your snippet functions using a for comprehension to gather any Boxed values and deal with Empty and Failure for you.

    for {
      user <- User.currentUser ?~ "You must be logged in to edit your profile."
    } yield ({
    ...
    }): NodeSeq

See [Dealing with Boxed values in Lift snippets](http://www.eltimn.com/blog/001-dealing-with-boxed-values-in-snippets) for more details.

## NgJE and NgJsCmds

Angular JavaScript expressions and commands.

# License

Apache 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
