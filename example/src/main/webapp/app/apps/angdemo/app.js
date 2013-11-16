(function(window, angular, undefined) {
  "use strict";

  var app = angular.module('AngDemo', ['AngDemoServer']);

  app.config(['$routeProvider', function ($routeProvider) {

    $routeProvider
      .when('/form', {
        controller: 'FormController',
        templateUrl: '/angdemo/partials/form'
      })
      .when('/success', {
        controller: 'SuccessController',
        templateUrl: '/angdemo/partials/success'
      })
      .when('/warning', {
        controller: 'WarningController',
        templateUrl: '/angdemo/partials/warning'
      })
      .otherwise({ redirectTo: '/form' });
  }]);

  app.controller('FormController', ['$scope', 'ServerParams', 'ServerFuncs', function($scope, ServerParams, ServerFuncs) {

    $scope.textInput = ServerParams.x;

    $scope.saveForm = function() {
      if (this.textInput) {
        ServerFuncs.saveForm({ textInput: this.textInput });
      }
    };

    $scope.$on('reset-form', function() {
      $scope.$apply(function() {
        $scope.textInput = "";
      });
    });

  }]);

  app.controller('SuccessController', ['$scope', 'ServerFuncs', function($scope, ServerFuncs) {

    $scope.sendSuccess = ServerFuncs.sendSuccess;
  }]);

  app.controller('WarningController', ['$scope', function($scope) {

    $scope.showWarning = function() {
      $(document).trigger("add-alerts", {
        message: "<em>This is a warning!</em>",
        priority: "warning"
      });
    };

  }]);

  app.controller('NavbarController', ['$scope', '$location', function ($scope, $location) {

    $scope.getClass = function (path) {
      if ($location.path().substr(0, path.length) === path) {
        return true;
      }
      else {
        return false;
      }
    };
  }]);

})(this, angular);
