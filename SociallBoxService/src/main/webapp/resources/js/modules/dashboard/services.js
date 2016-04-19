'use strict';

var app = angular.module('Dashboard');

app.factory('DashboardService',
	    [ '$http', '$cookieStore', '$rootScope','$q','AuthenticationService',
	    function ($http, $cookieStore, $rootScope, $q,AuthenticationService) {
	    	
	    	var service = {};
	    	
	    	//Service function to get dashboard cards
	    	service.dashboardCards = function(userId){
	    		var deferred = $q.defer();
	    		var dashboardCardsUrl = '/SociallBox/api/secured/users/organizers/admins/'+userId+'/dashboard';
	    		
	    		//Get auth token to pass in request
	    		return AuthenticationService.getAuthToken()
	    		.then(function(tokenResponse){
	    			//Extract epoch time and token from response
	    			var epoch = tokenResponse.epoch;
	    			var token = tokenResponse.token;
	    			
	    			//Make Call to get dashboard date
	    			 return $http({
			 	 				method:'GET',
			 	 				url: dashboardCardsUrl,
			 	 	            headers: {
			 	 	                    "Content-Type"		: 	"application/json",
			 	 						"accept"			:	"application/json",
			 	 	                    "X-Login-Ajax-call"	: 	'true',
			 	 	                    "Authorization"		:	token , 
				 	                    "X-Auth-Date" 		: 	epoch
			 	 	            }
			 	    		 })
			 	    		 .then(function(dashboardResponse) {
				                 if (dashboardResponse.status == 200) {
				                	 deferred.resolve(dashboardResponse);
				 					 return deferred.promise;
				                 }
			 	    		 }).catch(function(dashboardResponse){
			 	    			 console.log('Unable to get dashboard cards data . Response :'+dashboardResponse.status);
			 	    			 deferred.reject(dashboardResponse);
			 					 return deferred.promise;
			 	    		});
	    		}).catch(function(response){
	    			//If unable to get auth token, then redirect to login page
	    			console.log('Inside DashboardService.dashboardCards to gen token.Response :'+tokenResponse.status);
	    			
	    		});
	    	};
	    	
	    	service.getMonthlyUsers = function(userId){
	    		var deferred = $q.defer();
	    		var monthlyUsersURL = "/SociallBox/api/secured/users/organizers/admins/"+userId+"/dashboard/monthly/attendees";
	    		return AuthenticationService.getAuthToken()
	    		.then(function(tokenResponse){
	    			//Extract epoch time and token from response
	    			var epoch = tokenResponse.epoch;
	    			var token = tokenResponse.token;
	    			return $http({
	 	 				method:'GET',
	 	 				url: monthlyUsersURL,
	 	 	            headers: {
	 	 	                    "Content-Type"		: 	"application/json",
	 	 						"accept"			:	"application/json",
	 	 	                    "X-Login-Ajax-call"	: 	'true',
	 	 	                    "Authorization"		:	token , 
		 	                    "X-Auth-Date" 		: 	epoch
	 	 	            }
	 	    		 })
	 	    		 .then(function(dashboardResponse) {
		                 if (dashboardResponse.status == 200) {
		                	 deferred.resolve(dashboardResponse);
		 					 return deferred.promise;
		                 }
	 	    		 }).catch(function(dashboardResponse){
	 	    			 console.log('Unable to get dashboard chart data . Response :'+dashboardResponse.status);
	 	    			 deferred.reject(dashboardResponse);
	 					 return deferred.promise;
	 	    		});
	    			
	    		}).catch(function(response){
	    			//If unable to get auth token, then redirect to login page
	    			console.log('Inside DashboardService.dashboardCards to gen token.Response :'+tokenResponse.status);
	    			
	    		});
	    	};
	    	
	    	
	    	service.getMessagesForUser = function(userId){
	    		var deferred = $q.defer();
	    		var userMessagesUrl = "/SociallBox/api/secured/users/organizers/admins/"+userId+"/messages";
	    		return AuthenticationService.getAuthToken()
	    		.then(function(tokenResponse){
	    			//Extract epoch time and token from response
	    			var epoch = tokenResponse.epoch;
	    			var token = tokenResponse.token;
	    			
	    			return $http({
	 	 				method:'GET',
	 	 				url: userMessagesUrl,
	 	 	            headers: {
	 	 	                    "Content-Type"		: 	"application/json",
	 	 						"accept"			:	"application/json",
	 	 	                    "X-Login-Ajax-call"	: 	'true',
	 	 	                    "Authorization"		:	token , 
		 	                    "X-Auth-Date" 		: 	epoch
	 	 	            }
	 	    		 })
	 	    		 .then(function(dashboardResponse) {
		                 if (dashboardResponse.status == 200) {
		                	 deferred.resolve(dashboardResponse);
		 					 return deferred.promise;
		                 }
	 	    		 }).catch(function(dashboardResponse){
	 	    			 console.log('Unable to get user messages . Response :'+dashboardResponse.status);
	 	    			 deferred.reject(dashboardResponse);
	 					 return deferred.promise;
	 	    		});
	    		}).catch(function(response){
	    			//If unable to get auth token, then redirect to login page
	    			console.log('Inside DashboardService.dashboardCards to gen token.Response :'+tokenResponse.status);
	    			
	    		});
	    	};
	    	return service;
	    }
	])