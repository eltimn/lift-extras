App.namespace("views.knockout");
App.views.knockout.KnockoutExampleMod = (function($, ko) {
  "use strict";

  // private stuff
  var saveFunc = function() {};

  // the instance to return
  var inst = {};

  inst.init = function(_sendSuccess, _saveFunc) {
    saveFunc = _saveFunc;
    inst.sendSuccess = _sendSuccess;
  };

  inst.textInput = ko.observable("");

  inst.submitForm = function() {
    var ret = { textInput: inst.textInput() };
    // call the passed in save function with the form data as an argument.
    saveFunc(ret);
  };

  inst.sendSuccess = function() {};

  inst.showWarning = function() {
    // sends a notice to the client
    $(document).trigger("add-alerts", {message: "This is a warning!", priority: "warning"});
  };

  return inst;
}(jQuery, ko));
