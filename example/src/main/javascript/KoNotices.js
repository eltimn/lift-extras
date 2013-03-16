/* jshint unused:false */
var KoNotices = (function($, ko) {
  "use strict";

  // private vars
  var settings = {
    titles: {}
  };

  // private funcs
  function splitNotices(notices) {
    return {
      errs: ko.utils.arrayFilter(notices, function(it) {
        return it.priority === "error";
      }),
      warns: ko.utils.arrayFilter(notices, function(it) {
        return it.priority === "warning";
      }),
      infos: ko.utils.arrayFilter(notices, function(it) {
        return (it.priority === "notice" || it.priority === "info");
      }),
      succs: ko.utils.arrayFilter(notices, function(it) {
        return it.priority === "success";
      })
    };
  }

  var inst = {};

  // public vars
  inst.isVisible = ko.observable(true);

  inst.errors = ko.observableArray([]);
  inst.warnings = ko.observableArray([]);
  inst.infos = ko.observableArray([]);
  inst.succs = ko.observableArray([]);

  inst.errorTitle = ko.observable("");
  inst.warningTitle = ko.observable("");
  inst.infoTitle = ko.observable("");
  inst.successTitle = ko.observable("");

  // public funcs
  inst.init = function(data) {
    settings = $.extend({}, settings, data);

    // set the titles
    if (settings.titles.error) {
      inst.errorTitle(settings.titles.error);
    }

    if (settings.titles.warning) {
      inst.warningTitle(settings.titles.warning);
    }

    if (settings.titles.info) {
      inst.infoTitle(settings.titles.info);
    }

    if (settings.titles.success) {
      inst.successTitle(settings.titles.success);
    }

    $(document).on("add-notices", function(event, data) {
      var notices = Array.prototype.slice.call(arguments, 1);
      inst.addNotices(notices);
    });

    $(document).on("clear-notices", function(event) {
      inst.clearNotices();
    });
  };

  inst.clearNotices = function() {
    inst.errors([]);
    inst.warnings([]);
    inst.infos([]);
    inst.succs([]);
  };

  inst.addNotices = function(data) {
    var notices = splitNotices([].concat(data));

    ko.utils.arrayForEach(notices.errs, function(it) {
      inst.errors.push(it);
    });
    ko.utils.arrayForEach(notices.warns, function(it) {
      inst.warnings.push(it);
    });
    ko.utils.arrayForEach(notices.infos, function(it) {
      inst.infos.push(it);
    });
    ko.utils.arrayForEach(notices.succs, function(it) {
      inst.succs.push(it);
    });
  };

  inst.clearErrors = function() {
    inst.errors([]);
  };

  inst.clearWarnings = function() {
    inst.warnings([]);
  };

  inst.clearInfos = function() {
    inst.infos([]);
  };

  inst.clearSuccs = function() {
    inst.succs([]);
  };

  return inst;
}(jQuery, ko));
