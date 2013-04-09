/*! BsNotify version: 0.1.0
*  2013-04-10
*  Author: Tim Nelson
*  Website: https://github.com/eltimn/lift-extras
*  MIT License http://www.opensource.org/licenses/mit-license.php
*/
var BsNotify = (function($) {
  "use strict";

  // private vars
  var settings = {
    selector: "#bs-notifier",
    closable: true,
    transition: "fade",
    fadeOut: {
      enabled: true,
      delay: 5000
    },
    ids: []
  };

  function bsPriority(it) {
    if (it === "notice") {
      return "info";
    }
    else if (it === "danger") {
      return "error";
    }
    return it;
  }

  var inst = {};

  // public funcs
  inst.init = function(data) {
    settings = $.extend({}, settings, data);

    $(document).on("add-alerts", function(event, data) {
      var alerts = Array.prototype.slice.call(arguments, 1);
      $.each(alerts, function(index, value) {
        inst.notify(value);
      });
    });

    $(document).on("clear-alerts", function(event) {
      $(settings.selector).html("");
    });

    $.each(settings.ids, function(ix, alert_id) {
      $(document).on("set-alert-id-"+alert_id, function() {
        var alerts = Array.prototype.slice.call(arguments, 1);
        $.each(alerts, function(index, value) {
          inst.notify(value);
        });
      });
    });
  };

  inst.notify = function(msg) {
    $(settings.selector).notify({
      message: {
        html: msg.message
      },
      type: bsPriority(msg.priority),
      closable: settings.closable,
      transition: settings.transition,
      fadeOut: settings.fadeOut
    }).show();
  };

  return inst;
}(jQuery));
