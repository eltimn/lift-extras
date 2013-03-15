/* jshint unused:false */
var BsNotices = (function($, _) {
  "use strict";

  // private vars
  var settings = {
    elementId: "bs-notices",
    titles: {}
  };

  function splitNotices(notices) {
    return {
      errs: _.filter(notices, function(it) {
        return it.priority === "error";
      }),
      warns: _.filter(notices, function(it) {
        return it.priority === "warning";
      }),
      infos: _.filter(notices, function(it) {
        return (it.priority === "notice" || it.priority === "info");
      }),
      succs: _.filter(notices, function(it) {
        return it.priority === "success";
      })
    };
  }

  function bsPriority(it) {
    if (it === "notice") {
      return "info";
    }
    return it;
  }

  function attachLIs($ul, msgs) {
    _.each(msgs, function(it) {
      $ul.append($("<li/>").html(it.message));
    });
  }

  function containerId(priority) {
    return settings.elementId+"_"+priority;
  }

  function buildNoticeContainer(msgs) {
    if (msgs.length > 0) {

      var priority = bsPriority(msgs[0].priority);

      var $dismissBtn = $('<button/>', {
        'type':'button',
        'class':'close',
        'data-dismiss':'alert'
      }).html('&times;');

      var $ul = $("<ul/>");
      attachLIs($ul, msgs);

      var $container = $("<div/>", {
        "id": containerId(priority),
        "class": "alert alert-"+priority
      });

      $container.append($dismissBtn);

      var title = settings.titles[priority];

      if (title && title.length > 0) {
        var $title = $("<strong/>").html(title);
        $container.append($title);
      }

      return $container.append($ul);
    }

    return null;
  }

  function addNoticesToContainer(msgs) {
    if (msgs.length > 0) {
      var priority = bsPriority(msgs[0].priority);
      var $container = $("#"+containerId(priority));

      if ($container.length > 0) {
        var $ul = $container.find("ul");
        attachLIs($ul, msgs);
      }
      else {
        $container = buildNoticeContainer(msgs);
        $("#"+settings.elementId).append($container);
      }
    }
  }

  var inst = {};

  // public funcs
  inst.init = function(data) {
    settings = $.extend({}, settings, data);

    $(document).on("notices.add", function(event, data) {
      var notices = Array.prototype.slice.call(arguments, 1);
      inst.addNotices(notices);
    });

    $(document).on("notices.clear", function(event) {
      inst.clearNotices();
    });
  };

  inst.clearNotices = function() {
    $("#"+settings.elementId).html("");
  };

  inst.addNotices = function(data) {
    var notices = splitNotices([].concat(data));
    addNoticesToContainer(notices.errs);
    addNoticesToContainer(notices.warns);
    addNoticesToContainer(notices.infos);
    addNoticesToContainer(notices.succs);
  };

  return inst;
}(jQuery, _));
