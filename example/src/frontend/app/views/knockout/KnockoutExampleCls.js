App.namespace("views.knockout");
App.views.knockout.KnockoutExampleCls = function(sendSuccess, sendError, saveFunc) {
  "use strict";

  var self = this;

  self.textInput = ko.observable("");

  self.submitForm = function() {
    var ret = { textInput: self.textInput() };
    // call the passed in save function with the form data as an argument.
    saveFunc(ret);
  };

  self.sendSuccess = sendSuccess;
  self.sendError = sendError;

  self.showWarning = function() {
    // sends a notice to the client
    $(document).trigger("add-alerts", {message: "<em>This is a warning!</em>", priority: "warning"});
  };
};
