/**
  * jQuery plugin for rendering id notices in a Lift webapp.
  */
;(function($, window, document, undefined) {
  "use strict";

  /* BsIdNotices class definition
   * ========================== */
  var BsIdNotices = function(element, options) {
    var self = this;

    this.element = element;
    this.options = $.extend({}, $.fn.bsIdNotices.defaults, options);

    $(document).on("noticeid-"+this.options.noticeid+".set", function() {
      /**
        * Event data is passed in as multiple params, we
        * want them all as an array starting at the second one.
        * The first param is the event.
        */
      var msgs = Array.prototype.slice.call(arguments, 1);
      self.renderNotices(msgs);
    });

    $(document).on("noticeid-"+this.options.noticeid+".clear", function() {
      self.clear();
    });

    $(document).on("noticeid.clearall", function() {
      self.clear();
    });
  };

  BsIdNotices.prototype = {
    constructor: BsIdNotices,
    clear: function() {
      var $this = $(this.element);
      var $controlGroup = controlGroup($this);
      clearControlGroup($controlGroup);
      $this.html("");
    },
    renderNotices: function(msgs) {
      var $this = $(this.element);

      if (msgs.length > 0) {
        var $controlGroup = controlGroup($this);
        var priority = highestPriority(msgs);

        // clear element
        clearControlGroup($controlGroup);
        $this.html("");

        var nonSuccessMsgs = function() {
          return _.filter(msgs, function(it) {
            return it.priority !== "success";
          });
        };

        if (priority !== "success" || nonSuccessMsgs().length > 0) {
          var $ul = $("<ul/>");

          _.each(msgs, function(it) {
            var $li = $("<li/>", {
              'class': bsPriority(it.priority)
            }).html(it.message);
            $ul.append($li);
          });

          $this.append($ul);
        }

        $controlGroup.addClass(bsPriority(priority));
      }
    }
  };

  // "private" functions
  function bsPriority(it) {
    if (it === "notice") {
      return "info";
    }
    return it;
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

  function controlGroup($ele) {
    return $ele.closest("div.control-group");
  }

  function clearControlGroup($ele) {
    $ele.removeClass("info");
    $ele.removeClass("warning");
    $ele.removeClass("error");
    $ele.removeClass("success");
  }

  /* BsIdNotices plugin definition
   * ===================== */

  var old = $.fn.bsIdNotices;

  $.fn.bsIdNotices = function(option) {
    return this.each(function () {
      var $this = $(this),
          data = $this.data('bsIdNotices'),
          options = typeof option === 'object' && option;

      if (!data) {
        $this.data('bsIdNotices', (data = new BsIdNotices(this, options)));
      }
      if (typeof option === 'string') {
        data[option]();
      }
    });
  };

  $.fn.bsIdNotices.Constructor = BsIdNotices;

  $.fn.bsIdNotices.defaults = {
    noticeid: "lift-notice"
  };


  /* BsIdNotices no conflict
   * =============== */

  $.fn.bsIdNotices.noConflict = function () {
    $.fn.bsIdNotices = old;
    return this;
  };


  /* BsIdNotices data-api
   * ============ */

  $(document).ready(function () {
    $('[data-noticeid]').each(function () {
      var $ele = $(this);
      $ele.bsIdNotices($ele.data());
    });
  });

})(jQuery, window, document);
