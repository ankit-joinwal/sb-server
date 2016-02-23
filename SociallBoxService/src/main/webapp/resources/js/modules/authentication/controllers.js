'use strict';

var app = angular.module('Authentication');

app.controller('LoginController',
    ['$scope', '$rootScope', '$routeParams','$location','$facebook', 'AuthenticationService',
    function ($scope, $rootScope, $routeParams,$location, $facebook,AuthenticationService) {
       
	   var window_location = $location.path();
	   console.log('window_location:'+window_location);
	   if(window_location.indexOf("/rd/") > -1){
		   console.log('Redirect Case');
		  
		   var loginStatus = false;
		   AuthenticationService.isUserLoggedIn(function(authStatus){
			   if(authStatus.status == 200){
				   loginStatus = true;
			   }
		   });
		   if(!loginStatus){
			   AuthenticationService.promptUserToLogin("FACEBOOK").then(function(authResponse){
					console.log('Inside LoginController.redirectHandler Response :'+authResponse.status);
					
					window_location = window_location.replace("/rd/", "");
					console.log('Redirecting to '+window_location);
					$location.path(window_location);
				
				});
		   }else{
			   window_location = window_location.replace("/rd/", "");
					console.log('Redirecting to '+window_location);
					$location.path(window_location);
		   }
	   }
		
		$scope.fbLoginToggle = function() {
			console.log('Inside LoginController.fbLoginToggle , $rootScope.loginStatus='+$rootScope.loginStatus);
		  if($rootScope.loginStatus) {
			AuthenticationService.logout("FACEBOOK",function(){
				$location.path('/');
			});
		  } else {
			AuthenticationService.promptUserToLogin("FACEBOOK").then(function(authResponse){
				console.log('Inside LoginController.fbLoginToggle Response :'+authResponse.status);
			});
		  }
		};
		
		
    }]);
	

app.controller('LogoutController',
    ['$scope', '$rootScope', '$location', 'AuthenticationService',
    function ($scope, $rootScope, $location, AuthenticationService) {
        // reset login status
        AuthenticationService.ClearCredentials();
			console.log('Logout');
			$rootScope.authenticated = false;
			$location.path("/login");
		
    }]);