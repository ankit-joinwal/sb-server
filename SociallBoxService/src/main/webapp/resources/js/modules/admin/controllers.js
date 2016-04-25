'use strict';

angular.module('Admin')
.controller('AdminController',['$window','$scope', '$rootScope', '$routeParams','$location','AdminService',
      function ($window,$scope, $rootScope, $routeParams,$location,AdminService) {

	$scope.adminLogin = function(){
		$('#login-div').addClass('loader');
		var emailId = $scope.loginEmail;
		var password = $scope.loginPass;
		console.log('inside AdminAuthController for signin');
		AdminService.signin(emailId,password,false)
		.then(function(authResponse){
			console.log('Inside AdminAuthController.signin Response :'+authResponse.status);
			$window.location.href = "/SociallBox/nimda/home";
		})
		.catch(function(authResponse){
			console.log('Inside AdminAuthController.signin Response :'+authResponse.status);
			$('#login-div').removeClass('loader');
			alert("Invalid Credentials !!!");
		});
	};
	
	$scope.logout = function(){
		AdminService.clearProfile();
		$window.location.href = "/SociallBox/nimda/login";
	};
	
	$scope.getProfileData = function(){
		AdminService.isUserLoggedIn()
		.then(function(response){
			console.log('Inside AdminController.isUserLoggedIn Response :'+response.status);
			AdminService.getUserProfile()
			.then(function(profileResponse){
				var profile = profileResponse.data;
				$scope.userName = profile.name;
				$scope.emailId = profile.emailId;
				$scope.profilePic = profile.profilePic;
				$scope.userId = profile.userId;
			});
			
		})
		.catch(function(response){
			console.log('Inside AdminController.isUserLoggedIn Response :'+response.status);
			$window.location.href = "/SociallBox/nimda/login";
		});
	};
  	
	$scope.getAllEOProfiles = function(){
		AdminService.getEOProfiles()
		.then(function(response){
			$scope.profiles = response.data.data;
		});
	};
	
	$scope.getPendingProfiles = function(){
		AdminService.getPendingProfiles()
		.then(function(response){
			$scope.pending_profiles = response.data.data;
		});
	};
	
	$scope.goToCompanyDetails = function(profileId){
		$window.location.href = "/SociallBox/nimda/home#/organizers/"+profileId;
	};
	
	$scope.goToAdminHome = function(organizerId){
		$window.location.href = "/SociallBox/nimda/home#/organizers";
	};
	
	$scope.getCompanyDetails = function(){
		var profileId = $routeParams.profileId;
		
		AdminService.getCompanyDetails(profileId)
		.then(function(companyResponse){
			$scope.companyProfile = companyResponse.data.data.company_profile;
			$scope.eoAdminProfile = companyResponse.data.data;
			$('#company-profile-div').removeClass('loader');
		})
		.catch(function(response){
			console.log('Inside AdminController.getCompanyDetails Response :'+response.status);
			$window.location.href = "/SociallBox/nimda/home";
		});
	};
	
	$scope.approveProfile = function(profileId){
		$scope.msg = null;
		var profileIds = [];
		profileIds.push(profileId);
		AdminService.approveCompanyProfile(profileIds)
		.then(function(approveResponse){
			console.log(JSON.stringify(approveResponse.data));
			AdminService.getPendingProfiles()
			.then(function(response){
				$scope.pending_profiles = response.data.data;
				alert('Profile approved succesfully')
				$window.location.href = "/SociallBox/nimda/home#/organizers";
				
			});
			
		})
		.catch(function(approveResponse){
			console.log('Inside AdminController.approveProfile Response :'+approveResponse.status);
			alert('Unable to approve profile.Error occured');
			$window.location.href = "/SociallBox/nimda/home#/organizers";
		});
	}
  }
  ]);