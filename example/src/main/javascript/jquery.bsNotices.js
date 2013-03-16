/**
  * jQuery plugin for rendering id notices in a Lift webapp.
  */
;(function($, window, document, undefined) {
  "use strict";

  /* BsNotices class definition
   * ========================== */
  var BsNotices = function(element, options) {
    var self = this;

    this.element = element;
    this.options = $.extend({}, $.fn.bsNotices.defaults, options);

    $(document).on("notices.add", function() {
      var notices = Array.prototype.slice.call(arguments, 1);
      self.addNotices(notices);
    });

    $(document).on("notices.clear", function() {
      self.clearNotices();
    });
  };

  BsNotices.prototype = {
    constructor: BsNotices,
    clearNotices: function() {
      $(this.element).html("");
    },
    addNotices: function(data) {
      var notices = splitNotices([].concat(data));
      this.addNoticesToContainer(notices.errs);
      this.addNoticesToContainer(notices.warns);
      this.addNoticesToContainer(notices.infos);
      this.addNoticesToContainer(notices.succs);
    },
    buildNoticeContainer: function(msgs) {
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
          "data-notices-container": priority,
          "class": "alert alert-"+priority
        });

        $container.append($dismissBtn);

        var title = this.options.titles[priority];

        if (title && title.length > 0) {
          $container.append($("<strong/>").html(title));
        }

        return $container.append($ul);
      }

      return null;
    },
    addNoticesToContainer: function(msgs) {
      if (msgs.length > 0) {
        var $ele = $(this.element);
        var priority = bsPriority(msgs[0].priority);
        var $container = $("[data-notices-container='"+priority+"']", $ele);

        if ($container.length > 0) {
          var $ul = $container.find("ul");
          attachLIs($ul, msgs);
        }
        else {
          $container = this.buildNoticeContainer(msgs);
          $ele.append($container);
        }
      }
    }
  };

  // "private" funcs
  function splitNotices(notices) {
    return {
      errs: $.grep(notices, function(it) {
        return (it.priority === "error" || it.priority === "danger");
      }),
      warns: $.grep(notices, function(it) {
        return it.priority === "warning";
      }),
      infos: $.grep(notices, function(it) {
        return (it.priority === "notice" || it.priority === "info");
      }),
      succs: $.grep(notices, function(it) {
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
    $.each(msgs, function(ix, it) {
      $ul.append($("<li/>").html(it.message));
    });
  }

  /* BsNotices plugin definition
   * ===================== */

  var old = $.fn.bsNotices;

  $.fn.bsNotices = function(option) {
    return this.each(function () {
      var $this = $(this),
          data = $this.data('bsNotices'),
          options = typeof option === 'object' && option;

      if (!data) {
        $this.data('bsNotices', (data = new BsNotices(this, options)));
      }
      if (typeof option === 'string') {
        data[option]();
      }
    });
  };

  $.fn.bsNotices.Constructor = BsNotices;

  $.fn.bsNotices.defaults = {
    titles: {}
  };

  /* BsNotices no conflict
   * =============== */

  $.fn.bsNotices.noConflict = function () {
    $.fn.bsNotices = old;
    return this;
  };


  /* BsNotices data-api
   * ============ */

  $(document).ready(function () {
    $('[data-notices="alerts"]').each(function () {
      var $ele = $(this);
      $ele.bsNotices($ele.data());
    });
  });

}(jQuery, window, document));
