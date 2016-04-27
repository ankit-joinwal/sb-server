'use strict';

//Declare Modules
angular.module('index', []);
angular.module('Authentication', []);
angular.module('Dashboard', []);
angular.module('Company', []);
angular.module('Events', []);
angular.module('DateTime', []);

var App = angular.module('sociallbox',['ui.bootstrap', 'ui.bootstrap.datetimepicker','ngRoute','ngCookies','index','Authentication','Dashboard','Company','Events','DateTime']);

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
		.when('/events', {
			controller : "EventsController",
			templateUrl: '/SociallBox/eo/events/list'
		})
		.when('/events/new', {
			controller : "EventsController",
			templateUrl: '/SociallBox/eo/events/new'
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

