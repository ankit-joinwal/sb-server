'use strict';

var app = angular.module('Dashboard');

app.controller('DashboardController',['$window','$scope', '$rootScope', '$routeParams','$location','AuthenticationService',
                                 function ($window,$scope, $rootScope, $routeParams,$location,AuthenticationService) {
	
	console.log("Inside DashboardController");
	
	AuthenticationService.isUserLoggedIn()
	.then(function(response){
		console.log('Inside DashboardController.isUserLoggedIn Response :'+response.status);
		$rootScope.user_logged_in = true;
	})
	.catch(function(response){
		console.log('Inside DashboardController.isUserLoggedIn Response :'+response.status);
		
		$window.location.href = "/SociallBox/eo/login";
	});
}
]);