'use strict';

var app = angular.module('Company');

app.factory('CompanyService',
	    [ '$http', '$cookieStore', '$rootScope','$q','AuthenticationService',
	    function ($http, $cookieStore, $rootScope, $q,AuthenticationService) {
	    	var service = {};
	    	
	    	
	    	service.createCompanyProfile = function(userId,createCompanyRequest){
	    		var deferred = $q.defer();
	    		var createCompanyURL = "/SociallBox/api/secured/users/organizers/admins/"+userId+"/company";
	    		return AuthenticationService.getAuthToken()
	    		.then(function(tokenResponse){
	    			//Extract epoch time and token from response
	    			var epoch = tokenResponse.epoch;
	    			var token = tokenResponse.token;
	    			
	    			//Make API call to create company
	    			return $http({
	 	 				method:'POST',
	 	 				url: createCompanyURL,
	 	 	            data: createCompanyRequest,
	 	 	            headers: {
	 	 	                    "Content-Type"		: 	"application/json",
	 	 						"accept"			:	"application/json",
	 	 	                    "X-Login-Ajax-call"	: 	'true',
	 	 	                    "Authorization"		:	token , 
		 	                    "X-Auth-Date" 		: 	epoch
	 	 	            }
	 	    		 }).then(function(response) {
	 	                 if (response.status == 201) {
	 	                	
	 	                 	deferred.resolve(response);
	 	 					return deferred.promise;
	 	                 }else{
	 	                	 
	 	 					 deferred.reject(response);
	 	 					 return deferred.promise;
	 	                 }
	 	             });
	    		}).catch(function(response){
	    			//If unable to get auth token, then redirect to login page
	    			console.log('Inside CompanyService.createCompanyProfile to gen token.Response :'+response.status);
	    		});
	    	};
	    	
	    	
	    	service.uploadCompanyPhoto = function(userId,companyId,file,photoType){
	    		var deferred = $q.defer();
	    		var uploadUrl = "/SociallBox/api/secured/users/organizers/admins/"+userId+"/company/"+companyId+"/picture?type="+photoType
	    		return AuthenticationService.getAuthToken()
			    		.then(function(tokenResponse){
				    			//Extract epoch time and token from response
				    			var epoch = tokenResponse.epoch;
				    			var token = tokenResponse.token;
				    			 var fd = new FormData();
			    		        fd.append('files', file);
			    		        return $http.post(uploadUrl, fd, {
			    		            transformRequest: angular.identity,
			    		            headers: {'Content-Type': undefined, "X-Login-Ajax-call"	: 	'true',"accept"			:	"application/json",
			 	 	                    "Authorization"		:	token , 
				 	                    "X-Auth-Date" 		: 	epoch}
			    		        })
			    		        .then(function(uploadResponse) {
			    		        	if (uploadResponse.status == 201) {
				 	                	
				 	                 	deferred.resolve(uploadResponse);
				 	 					return deferred.promise;
			    		        	}else{
				 	                	 
				 	 					 deferred.reject(uploadResponse);
				 	 					 return deferred.promise;
			    		        	}
			    		        })
			    		        .catch(function(uploadResponse){
			    	    			//If unable to get auth token, then redirect to login page
			    	    			console.log('Inside CompanyService.uploadCompanyPhoto to upload photo.Response :'+tokenResponse.status);
			    	    			 deferred.reject(uploadResponse);
			 	 					 return deferred.promise;
			    	    		});
			    		}).catch(function(tokenResponse){
			    			//If unable to get auth token, then redirect to login page
			    			console.log('Inside CompanyService.uploadCompanyPhoto to gen token.Response :'+tokenResponse.status);
			    			deferred.reject(response);
			 				return deferred.promise;
			    		});
	    	};
	    	return service;
	    }
		]);