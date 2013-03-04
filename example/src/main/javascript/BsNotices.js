// http://www.adequatelygood.com/2010/3/JavaScript-Module-Pattern-In-Depth
var BsNotices = (function($, _) {
  "use strict";

  // private vars
  var debug = true;
  var showAll, elementId, titles;

  // private funcs
  function log(it) {
    if (debug) {
      console.log(it);
    }
  }

  function splitNotices(notices) {
    var _idns = [];
    if (!showAll) {
      _idns = _.filter(notices, function(it) {
        return it.id;
      })
    }

    return {
      errs: _.filter(notices, function(it) {
        return (it.priority === "error" && (showAll || !(it.id)));
      }),
      warns: _.filter(notices, function(it) {
        return (it.priority === "warning" && (showAll || !(it.id)));
      }),
      infos: _.filter(notices, function(it) {
        return (it.priority === "notice" && (showAll || !(it.id)));
      }),
      succs: _.filter(notices, function(it) {
        return (it.priority === "success" && (showAll || !(it.id)));
      }),
      idns: _idns
    }
  }

  function highestPriority(msgs) {
    var errCnt = _.filter(msgs, function(it) {
      return it.priority === "error";
    }).length;

    if (errCnt > 0) {
      return "error";
    }

    var warnCnt = _.filter(msgs, function(it) {
      return it.priority === "warning";
    }).length;

    if (warnCnt > 0) {
      return "warning";
    }

    var successCnt = _.filter(msgs, function(it) {
      return it.priority === "success";
    }).length;

    if (successCnt > 0) {
      return "success";
    }

    return "info";
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
    return elementId+"_"+priority;
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
      })

      $container.append($dismissBtn);

      var title = titles[priority];

      if (title && title.length > 0) {
        var $title = $("<strong/>").html(title);
        $container.append($title)
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
        attachLIs($ul, msgs)
      }
      else {
        $container = buildNoticeContainer(msgs);
        $("#"+elementId).append($container);
      }
    }
  }

  // Id notices

  function controlGroup($ele) {
    return $ele.closest("div.control-group");
  }

  function clearControlGroup($ele) {
    $ele.removeClass("info");
    $ele.removeClass("warning");
    $ele.removeClass("error");
    $ele.removeClass("success");
  }

  function renderNoticesForId(msgId, msgs) {
    if (msgs.length > 0) {
      var $container = $("[data-id-notice='"+msgId+"']");
      var $controlGroup = controlGroup($container);
      var priority = highestPriority(msgs);

      // clear element
      $container.html("");
      clearControlGroup($controlGroup);

      function nonSuccessMsgs() {
        return _.filter(msgs, function(it) {
          return it.priority !== "success";
        });
      }

      if (priority !== "success" || nonSuccessMsgs().length > 0) {
        var $ul = $("<ul/>");

        _.each(msgs, function(it) {
          var $li = $("<li/>", {
            'class': bsPriority(it.priority)
          }).html(it.message);
          $ul.append($li);
        });

        $container.append($ul);
      }

      $controlGroup.addClass(bsPriority(priority));
    }
  }

  function renderIdNotices(msgs) {
    var grouped = _.chain(msgs)
      .groupBy("id")
      .value();

    _.chain(grouped)
      .keys()
      .each(function(id) {
        renderNoticesForId(id, grouped[id]);
      });
  }

  var inst = {};

  // public vars

  // public funcs
  inst.init = function(data) {
    elementId = data.elementId || "bs-notices";
    showAll = data.showAll || false;
    titles = data.titles || {};
  };

  inst.clearNotices = function() {
    $("#"+elementId).html("");
  }

  inst.clearIdNotice = function(id) {
    $("[data-id-notice='"+id+"']").each(function() {
      var $ele = $(this);
      var $controlGroup = controlGroup($ele);
      $ele.html("");
      clearControlGroup($controlGroup);
    });
  };

  inst.clearIdNotices = function() {
    $("[data-id-notice]").each(function() {
      var $ele = $(this);
      var $controlGroup = controlGroup($ele);
      $ele.html("");
      clearControlGroup($controlGroup);
    });
  };

  inst.clearAll = function() {
    inst.clearNotices();
    inst.clearIdNotices();
  };

  inst.addNotices = function(data) {
    var notices = splitNotices([].concat(data));
    addNoticesToContainer(notices.errs);
    addNoticesToContainer(notices.warns);
    addNoticesToContainer(notices.infos);
    addNoticesToContainer(notices.succs);
    renderIdNotices(notices.idns);
  };

  inst.setNotices = function(data) {
    var notices = splitNotices([].concat(data));
    var $element = $("#"+elementId);

    // clear html
    $element.html("");

    var $errs = buildNoticeContainer(notices.errs);
    if ($errs) {
      $element.append($errs);
    }

    var $warns = buildNoticeContainer(notices.warns);
    if ($warns) {
      $element.append($warns);
    }

    var $infos = buildNoticeContainer(notices.infos);
    if ($infos) {
      $element.append($infos);
    }

    var $succs = buildNoticeContainer(notices.succs);
    if ($succs) {
      $element.append($succs);
    }

    // clear id notices
    inst.clearIdNotices();

    // handle the id notices
    renderIdNotices(notices.idns);
  };

  return inst;
}(jQuery, _));
