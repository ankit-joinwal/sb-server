'use strict';

var app = angular.module('Authentication');

app.controller('AuthController',['$window','$scope', '$rootScope', '$routeParams','$location','AuthenticationService',
    function ($window,$scope, $rootScope, $routeParams,$location,AuthenticationService) {
	
    	console.log("Inside Auth Controller");
    	$scope.register = function(){
    		$('#login-div').addClass('loader');
    		var name = $scope.username;
    		var emailId = $scope.email;
    		var password = $scope.password;
    		var confirmPassword = $scope.confirmPassword;
    		
    		
    		AuthenticationService.register(name,emailId,password)
    		.then(function(authResponse){
				console.log('Inside AuthController.register Response :'+authResponse.status);
				
				$window.location.href = "/SociallBox/eo/home";
			})
			.catch(function(authResponse){
				console.log('Inside AuthController.register Response :'+authResponse.status);
				$('#login-div').removeClass('loader');
				alert("Registration Failed !");
			});
    	};
    	
    	$scope.login = function(){
    		$('#login-div').addClass('loader');
    		var emailId = $scope.loginEmail;
    		var password = $scope.loginPass;
    		
    		AuthenticationService.signin(emailId,password,false)
    		.then(function(authResponse){
				console.log('Inside AuthController.signin Response :'+authResponse.status);
				$window.location.href = "/SociallBox/eo/home";
			})
			.catch(function(authResponse){
				console.log('Inside AuthController.signin Response :'+authResponse.status);
				$('#login-div').removeClass('loader');
				alert("Invalide Credentials !!!");
			});
    	};
    	
    	$scope.logout = function(){
    		AuthenticationService.clearProfile();
    		$window.location.href = "/SociallBox/eo/login";
  		      	};
    	
    	$scope.initUserProfilePage = function(){
    		AuthenticationService.getUserProfile()
			.then(function(profileResponse){
				var profile = profileResponse.data;
				$scope.userName = profile.name;
				$scope.emailId = profile.emailId;
				$scope.profilePic = profile.profilePic;
				$scope.userId = profile.userId;
				$scope.encPassword = profile.password;
				$scope.newPassword = null;
				$scope.newUserName = null;
			});
    	} ;
    	
    	$scope.editPtofile = function(){
    		$("#user-profile-container").addClass('loader');
    		var profilePic = $scope.newProfilePic;
    		var userId = $scope.userId;
    		var userName = $scope.newUserName;
    		var newPassword = $scope.newPassword;
    		AuthenticationService.editUserProfile(userId,userName,newPassword,profilePic)
			.then(function(editResponse){
				if (editResponse.status == 200) {
					
					if(newPassword != null){
						AuthenticationService.encryptPass(newPassword)
						.then(function(encryptedPass){
							AuthenticationService.setUserProfile(editResponse.data.data.id,editResponse.data.data.name,
									editResponse.data.data.email_id,editResponse.data.data.profile_id,
									editResponse.data.data.profile_pic,editResponse.data.data.status,
									encryptedPass,editResponse.data.data.company_profile)
				        	.then(function(){
				        		$window.location.reload();
				        	});
						});
					}else{
						
						var password = $scope.encPassword;
						AuthenticationService.setUserProfile(editResponse.data.data.id,editResponse.data.data.name,
								editResponse.data.data.email_id,editResponse.data.data.profile_id,
								editResponse.data.data.profile_pic,editResponse.data.data.status,
								password,editResponse.data.data.company_profile)
			        	.then(function(){
			        		//$window.location.href = "/SociallBox/eo/home#/profile";
			        		$window.location.reload();
			        	});
					}
					
				}
				
				
			})
			.catch(function(editResponse){
				console.log('Error in creating company profile . Response :'+editResponse.status);
			});
    		
    	}
	}
]);