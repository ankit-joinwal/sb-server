'use strict';

//Declare Modules
angular.module('index', []);
angular.module('Authentication', []);
angular.module('Dashboard', []);
angular.module('Company', []);

var App = angular.module('sociallbox',['ngRoute','ngCookies','index','Authentication','Dashboard','Company']);

App.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/', {
			controller : "DashboardController",
			templateUrl: '/SociallBox/eo/dashboard'
		})
		.when('/dashboard', {
			controller : "DashboardController",
			templateUrl: '/SociallBox/eo/dashboard'
		})
		.when('/profile', {
			controller : "AuthController",
			templateUrl: '/SociallBox/eo/profile'
		})
		.when('/company', {
			controller : "CompanyController",
			templateUrl: '/SociallBox/eo/company'
		})
		.when('/company/new', {
			controller : "CompanyController",
			templateUrl: '/SociallBox/eo/company/new'
		})
		.otherwise({redirectTo:'/'});		
}]);

App.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

