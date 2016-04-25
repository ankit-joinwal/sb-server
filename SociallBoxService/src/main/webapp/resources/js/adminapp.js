'use strict';

//Declare Modules
angular.module('Admin', []);

var App = angular.module('sociallboxadmin',['ngRoute','ngCookies','Admin']);

App.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when('/', {
			controller : "AdminController",
			templateUrl: '/SociallBox/nimda/organizers'
		})
		.when('/organizers', {
			controller : "AdminController",
			templateUrl: '/SociallBox/nimda/organizers'
		})
		.when('/organizers/:profileId', {
            controller: 'AdminController',
            templateUrl: '/SociallBox/nimda/organizers/detail'
        })
        .when('/organizerlist', {
			controller : "AdminController",
			templateUrl: '/SociallBox/nimda/organizers/search'
		})
		.otherwise({redirectTo:'/'});		
}]);


