(function(window, $) {
  window.app = {
    notices: {
      /**
        * This must be called to clear out notices div during ajax calls. For idMessages.
        */
      clear: function() {
        $(".notices-container").each(function(index) {
          $(this).html("");
        });
        $("form div.control-group").each(function(index) {
          $(this).removeClass("error");
          $(this).removeClass("warning");
          $(this).removeClass("info");
        });
      },
      /**
        * Add css class to outer control-group div
        */
      onError: function(id, type) {
        var it = $("#"+id);
        if (it.html()) {
          it.closest("div.control-group").addClass(type);
        }
      }
    }
  };
})(this, jQuery);

