App.namespace("views");
App.views.ChatKnockOutJs = (function(ko) {
  "use strict";

  // private stuff
  var saveFunc = function() {};

  // the instance to return
  var inst = {};

  inst.init = function(_saveFunc, msgs) {
    saveFunc = _saveFunc;
    ko.utils.arrayForEach([].concat(msgs), function(it) {
      inst.messages.push(it);
    });
  };

  inst.newMessage = ko.observable("");
  inst.messages = ko.observableArray();
  inst.addMessage = function(newMessage) {
    inst.messages.push(newMessage);
  };

  inst.submitForm = function() {
    var ret = { message: inst.newMessage() };
    saveFunc(ret);
  };

  return inst;
}(ko));

