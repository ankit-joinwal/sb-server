'use strict';

var app = angular.module('Authentication');

app.controller('AuthController',['$window','$scope', '$rootScope', '$routeParams','$location','AuthenticationService',
    function ($window,$scope, $rootScope, $routeParams,$location,AuthenticationService) {
	
    	console.log("Inside Auth Controller");
    	$scope.register = function(){
    		
    		var name = $scope.username;
    		var emailId = $scope.email;
    		var password = $scope.password;
    		var confirmPassword = $scope.confirmPassword;
    		
    		
    		AuthenticationService.register(name,emailId,password)
    		.then(function(authResponse){
				console.log('Inside AuthController.register Response :'+authResponse.status);
				
				$window.location.href = "/SociallBox/eo/dashboard";
			})
			.catch(function(authResponse){
				console.log('Inside AuthController.register Response :'+authResponse.status);
				alert("Registration Failed !");
			});
    	}
    	
    	$scope.login = function(){
    		var emailId = $scope.loginEmail;
    		var password = $scope.loginPass;
    		
    		AuthenticationService.signin(emailId,password)
    		.then(function(authResponse){
				console.log('Inside AuthController.signin Response :'+authResponse.status);
				$window.location.href = "/SociallBox/eo/dashboard";
			})
			.catch(function(authResponse){
				console.log('Inside AuthController.signin Response :'+authResponse.status);
				alert("Invalide Credentials !!!");
			});
    	}
	}
]);