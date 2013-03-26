# Lift Extras Module

Some utils for developing Lift webapps.

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

A snippet that uses a Knockout.js `View Model` to display notices. Does not rely on Bootstrap. Allows for any html that complies with the `View Model`. See KoAlerts.scala and alerts.html for details.

### BsNotify

This is a JavaScript module that is a wrapper around [bootstrap-notify](http://nijikokun.github.com/bootstrap-notify/ "bootstrap-notify") that provides an interface to the events mentioned above.

## Snippets

### BsMenu

A snippet for rendering a Bootstrap menu using Groups.


## BootstrapScreen

A base LiftScreen trait that works with Bootstrap.

## Gravatar

An object for displaying Gravatar images.

## JsExtras

A few classes for passing callback functions to client-side JavaScript.

### AjaxCallbackAnonFunc

Creates an anonymous JavaScript function that calls a server side function, then runs the server-side functions return JsCmd on the client.

### JsonCallbackAnonFunc

Creates an anonymous JavaScript function that takes JSON as a parameter and calls a server side function, then runs the server-side functions return JsCmd on the client.

## SnippetExtras

A trait with a series of implicit functions that convert a Box(ed) value (NodeSeq, JsCmd, CssSel) to it's inner value if Full and will display an error otherwise. Uses LiftNotice.

It allows you to write your snippet functions using a for comprehension to gather any Boxed values and deal with Empty and Failure for you.

    for {
      user <- User.currentUser ?~ "You must be logged in to edit your profile."
    } yield ({
    ...
    }): NodeSeq

# License

Apache 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
