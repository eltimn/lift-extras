// source: http://addyosmani.com/blog/essential-js-namespacing/

// top-level namespace being assigned an object literal
var App = App || {};

// Some top level namespaces to start with
App.views = {};
App.utils = {};

/**
  * A convenience function for parsing string namespaces and
  * automatically generating nested namespaces.
  *
  * Example:
  * App.extend('modules.module2');
  *
  */
App.extend = function(ns_string) {
  var parts = ns_string.split('.'),
      parent = App,
      pl, i;

  pl = parts.length;
  for (i = 0; i < pl; i++) {
    //create a property if it doesnt exist
    if (typeof parent[parts[i]] === 'undefined') {
      parent[parts[i]] = {};
    }
    parent = parent[parts[i]];
  }
  return parent;
};

