// http://www.adequatelygood.com/2010/3/JavaScript-Module-Pattern-In-Depth
/**
  * Problems with this approach: tough to bind IdNotices to ViewModel. Would need to be bound to VM created on the Form.
  * Would require you to create a VM for a form.
   *
  * TODO:
  * Create a KoFormsModule. Extend it to create a custom form module.
  * create a KoFormSnippet for helping
  * how to send notices to the custom module ?? via a FactoryMaker - IdNoticesHandler. Allow adding multiple handlers - implement as PartialFunction
  */

var KoNotices = (function($, ko) {
  "use strict";

  // private vars
  var debug = true;
  var showAll;

  // private funcs
  function log(it) {
    if (debug) {
      console.log(it);
    }
  }

  function splitNotices(notices) {
    log("notices: "+$.dump(notices));

    var _ids = [];
    if (!showAll) {
      _ids = ko.utils.arrayFilter(notices, function(it) {
        return it.id;
      })
    }

    return {
      errs: ko.utils.arrayFilter(notices, function(it) {
        return (it.priority === "error" && (showAll || !(it.id)));
      }),
      warns: ko.utils.arrayFilter(notices, function(it) {
        return (it.priority === "warning" && (showAll || !(it.id)));
      }),
      infos: ko.utils.arrayFilter(notices, function(it) {
        return (it.priority === "notice" && (showAll || !(it.id)));
      }),
      ids: _ids
    }
  }

  var inst = {};

  // public vars
  inst.errors = ko.observableArray([]);
  inst.warnings = ko.observableArray([]);
  inst.infos = ko.observableArray([]);
  inst.idNotices = ko.observableArray([]);


  // public funcs
  inst.init = function(data) {
    showAll = data.showAll || false;

    // register all field err elements
    $("[data-id-notice]").each(function() {
      var $element = $(this);
      var fieldId = $element.attr("data-id-notice");
      log("element: "+$.dump($element));
      log("fieldId: "+fieldId);

      // create the computed observable
      inst["idnotice_"+fieldId] = ko.computed(function() {
        return ko.utils.arrayFilter(inst.idNotices(), function(it) {
          return it.id === fieldId;
        })
      });

      // apply ko bindings to the element
      ko.applyBindings(KoNotices, $element[0]);
    });
  };

  /*inst.registerIdNotice = function(element, id) {
    // create the computed observable
    inst["idnotice_"+id] = ko.computed({
      return ko.utils.arrayFilter(inst.idNotices(), function(it) {
        return it.id === id;
      })
    });

    // apply ko bindings to the element
    ko.applyBindings(KoNotices, element);
  }*/

  inst.addNotices = function(data) {
    var notices = splitNotices([].concat(data));

    log("errs: "+$.dump(notices.errs));
    ko.utils.arrayForEach(notices.errs, function(it) {
      inst.errors.push(it);
    });
    ko.utils.arrayForEach(notices.warns, function(it) {
      inst.warnings.push(it);
    });
    ko.utils.arrayForEach(notices.infos, function(it) {
      inst.infos.push(it);
    });
    ko.utils.arrayForEach(notices.ids, function(it) {
      inst.idNotices.push(it);
    });
  };

  inst.setNotices = function(data) {
    var notices = splitNotices([].concat(data));

    log("errs: "+$.dump(notices.errs));
    inst.errors(notices.errs);
    inst.warnings(notices.warns);
    inst.infos(notices.infos);
    inst.idNotices(notices.ids);
  };

  inst.clearIdNotice = function(id) {
    var filtered = ko.utils.arrayFilter(inst.idNotices(), function(it) {
      return it.id !== id;
    })

    inst.idNotices(filtered);
  };

  inst.setIdNotice = function(data) {
    inst.clearIdNotice(data.id);
    inst.idNotices.push(data);
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

  return inst;
}(jQuery, ko));


/**
  * A knockout custom binding for notices with ids. They are registered and bound with the KoNotices module.
  */
(function($) {
  function highestPriority(msgs) {
    var errCnt = ko.utils.arrayFilter(msgs, function(it) {
      return it.priority === "error";
    }).length;

    if (errCnt > 0) {
      return "error";
    }

    var warnCnt = ko.utils.arrayFilter(msgs, function(it) {
      return it.priority === "warning";
    }).length;

    if (warnCnt > 0) {
      return "warning";
    }

    var successCnt = ko.utils.arrayFilter(msgs, function(it) {
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

  ko.bindingHandlers.idnotice = {
    init: function(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
      // register

    },
    update: function(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
      var $element = $(element);
      var $controlGroup = $element.closest("div.control-group");
      var notices = [].concat(ko.utils.unwrapObservable(valueAccessor()));
      var priority = highestPriority(notices);

      // clear element
      $element.html("");
      $element.removeClass();
      $controlGroup.removeClass("info");
      $controlGroup.removeClass("warning");
      $controlGroup.removeClass("error");
      $controlGroup.removeClass("success");

      if (notices.length > 0) {
        var $ul = $("<ul/>");

        ko.utils.arrayForEach(notices, function(it) {
          var $li = $("<li/>", {
            'class': bsPriority(it.priority)
          }).html(it.message);
          $ul.append($li);
        });

        $element.append($ul);

        if (priority) {
          $controlGroup.addClass(bsPriority(priority));
        }
      }
    }
  };
})(jQuery);
