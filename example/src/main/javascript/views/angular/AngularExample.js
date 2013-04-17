(function(window, angular, undefined) {
  "use strict";

  angular.module('views.angular.AngularExample', ['views.angular.AngularExampleServer'])
    .controller('PageCtrl', ['$scope', 'ServerFuncs', 'ServerParams', function($scope, ServerFuncs, ServerParams) {
      $scope.textInput = ServerParams.x;
      $scope.sendSuccess = ServerFuncs.sendSuccess;

      $scope.saveForm = function() {
        if (this.textInput) {
          ServerFuncs.saveForm({ textInput: this.textInput });
        }
      };

      $scope.showWarning = function() {
        $(document).trigger("add-alerts", {
          message: "<em>This is a warning!</em>",
          priority: "warning"
        });
      };

      $scope.$on('reset-form', function() {
        $scope.$apply(function() {
          $scope.textInput = "";
        });
      });

    }]);

})(this, angular);
