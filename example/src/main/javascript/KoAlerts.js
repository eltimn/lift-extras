/*! KoAlerts version: 0.1.0
*  2013-04-10
*  Author: Tim Nelson
*  Website: https://github.com/eltimn/lift-extras
*  MIT License http://www.opensource.org/licenses/mit-license.php
*/
var KoAlerts = (function($, ko) {
  "use strict";

  // private vars
  var settings = {
    titles: {},
    ids: []
  };

  // private funcs
  function splitAlerts(alerts) {
    return {
      errs: ko.utils.arrayFilter(alerts, function(it) {
        return (it.priority === "error" || it.priority === "danger");
      }),
      warns: ko.utils.arrayFilter(alerts, function(it) {
        return it.priority === "warning";
      }),
      infos: ko.utils.arrayFilter(alerts, function(it) {
        return (it.priority === "notice" || it.priority === "info");
      }),
      succs: ko.utils.arrayFilter(alerts, function(it) {
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

    $(document).on("add-alerts", function(event, data) {
      var alerts = Array.prototype.slice.call(arguments, 1);
      inst.addAlerts(alerts);
    });

    $(document).on("clear-alerts", function(event) {
      inst.clearAlerts();
    });

    $.each(settings.ids, function(ix, alert_id) {
      $(document).on("set-alert-id-"+alert_id, function() {
        var msgs = Array.prototype.slice.call(arguments, 1);
        inst.addAlerts(msgs);
      });
    });
  };

  inst.clearAlerts = function() {
    inst.errors([]);
    inst.warnings([]);
    inst.infos([]);
    inst.succs([]);
  };

  inst.addAlerts = function(data) {
    var alerts = splitAlerts([].concat(data));

    ko.utils.arrayForEach(alerts.errs, function(it) {
      inst.errors.push(it);
    });
    ko.utils.arrayForEach(alerts.warns, function(it) {
      inst.warnings.push(it);
    });
    ko.utils.arrayForEach(alerts.infos, function(it) {
      inst.infos.push(it);
    });
    ko.utils.arrayForEach(alerts.succs, function(it) {
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
