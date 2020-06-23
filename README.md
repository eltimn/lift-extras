Extras Lift Module
==================

[![Build Status](https://travis-ci.org/eltimn/lift-extras.svg?branch=master)](https://travis-ci.org/eltimn/lift-extras)

<!-- [![Bintray](https://img.shields.io/bintray/v/eltimn/maven/lift-extras.svg)](https://bintray.com/eltimn/maven/lift-extras) -->

Some extras for developing Lift webapps.

# Installation

Releases use the Lift "edition" in the name. For example, if you use any of 2.5-SNAPSHOT, 2.5-RC4, or 2.5 the Lift edition is 2.5.

**Note:** some of the older versions published had a '-' in the edition instead of a '.'. I.e *extras_3-0* instead of *extras_3.0*.

To include this module in your Lift project change `build.sbt` to include:

    libraryDependencies += "net.liftmodules" %% "extras_3.3" % "1.1.3"

Previous releases
-----------------

| Lift Version | Scala Version | Module Version |
|--------------|---------------|----------------|
| 3.4          |  2.12, 2.13   | 1.1.4          |
| 3.3          |  2.12, 2.11   | 1.1.3          |
| 3.2          |  2.12, 2.11   | 1.1.3          |
| 3.1          |  2.12, 2.11   | 1.1.3          |
| 3.0          |  2.12, 2.11   | 1.0.1          |
| 2.6          |  2.10, 2.9    | 0.4            |
| 2.5          |  2.10, 2.9    | 0.3            |


# Documentation

## LiftNotice

A case class that models a Lift notice and a companion object that provides:

* conversion functions to JValue and JsCmd
* a function for overriding LiftRules.noticesToJsCmd
* a `success` notice type

The `noticesToJsCmd` function returns a JsCmd that sends jQuery events to the browser. This makes it easy to write custom JavaScript that listens for the events and displays the notices.

The main motivation for writing this was to have a way to display alerts directly from JavaScript and not have to call the server. And I didn't want to have to duplicate code. H/T to @fmpwizard and @shadowfiend for the idea to use events.

See the documentation for the two jQuery plugins for more details on the events.

### [jQuery-bsAlerts](https://eltimn.github.com/jquery-bs-alerts/index.html "jQuery-bsAlerts")

A jQuery plugin for displaying Bootstrap alerts via jQuery events. See also the companion BsAlerts snippet.

### [jQuery-bsFormAlerts](https://eltimn.github.com/jquery-bs-formalerts/index.html "jQuery-bsFormAlerts")

A jQuery plugin for displaying Bootstrap form alerts via jQuery events.

## BsMenu

A snippet for rendering a Bootstrap menu using Groups.

## BootstrapScreen

A base LiftScreen trait that works with Bootstrap. Requires accompanying wizard-all.html file.

## Gravatar

An object for displaying Gravatar images.

## JsExtras

A few classes for passing callback functions to client-side JavaScript. See [JavaScript Apps With Lift: Callback Functions](http://eltimn.com/post/004-javascript-apps-with-lift-callback-functions) for more information.

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

See [Dealing with Boxed values in Lift snippets](http://eltimn.com/post/001-dealing-with-boxed-values-in-snippets) for more details.

## NgJE and NgJsCmds

Angular JavaScript expressions and commands.

# License

Apache 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
