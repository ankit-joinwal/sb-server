'use strict';

angular.module('index', []);
angular.module('Admin', []);

var App = angular.module('adminlogin',['ngRoute','ngCookies','index','Admin']);

App.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/', {
			templateUrl: '/',
			controller : "AdminController"
		})
		.when('/nimda/login', {
			controller : "AdminController",
			templateUrl: '/nimda/login'
		})
		.when('/nimda/home', {
			templateUrl: '/nimda/home',
			controller : "AdminController"
		})
		.otherwise({redirectTo:'/'});		
}]);
