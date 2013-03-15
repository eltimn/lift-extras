App.namespace("views.knockout");
App.views.knockout.KnockoutExampleCls = function(saveFunc, sendSuccess) {
  "use strict";

  var self = this;

  self.textInput = ko.observable("");

  self.submitForm = function() {
    var ret = { textInput: self.textInput() };
    // call the passed in save function with the form data as an argument.
    saveFunc(ret);
  };

  self.sendSuccess = sendSuccess;

  self.showWarning = function() {
    // sends a notice to the client
    $(document).trigger("notices.add", {message: "This is a warning!", priority: "warning"});
  };
};
