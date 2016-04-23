'use strict';

angular.module('index', []);
angular.module('Authentication', []);

var App = angular.module('login',['ngRoute','ngCookies','index','Authentication']);

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
		.when('/eo/home', {
			templateUrl: '/eo/home',
			controller : "IndexController"
		})
		.otherwise({redirectTo:'/'});		
}]);
