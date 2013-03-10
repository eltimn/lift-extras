App.namespace("views.knockout");
App.views.knockout.KnockoutExample = (function(ko, BsNotices) {
  "use strict";

  // private stuff
  var saveFunc = function() {};

  // the instance to return
  var inst = {};

  inst.init = function(_saveFunc, _sendSuccess) {
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
    BsNotices.addNotices({message: "This is a warning!", priority: "warning"});
  };

  return inst;
}(ko, BsNotices));
