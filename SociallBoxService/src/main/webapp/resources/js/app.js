'use strict';

//Declare Modules
angular.module('index', []);
angular.module('Authentication', []);
angular.module('Dashboard', []);

var App = angular.module('sociallbox',['ngRoute','ngCookies','index','Authentication','Dashboard']);

App.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/', {
			templateUrl: '/',
			controller : "IndexController"
		})
		.when('/eo/login', {
			controller : "AuthController",
			templateUrl: '/eo/login'
		})
		.when('/eo/dashboard', {
			controller : "DashboardController",
			templateUrl: '/eo/dashboard'
		})
		.otherwise({redirectTo:'/'});		
}]);

