'use strict';

var app = angular.module('Company');

app.controller('CompanyController',
		['$window','$scope', '$rootScope', '$routeParams','$location','AuthenticationService','CompanyService',
        function ($window,$scope, $rootScope, $routeParams,$location,AuthenticationService,CompanyService) {
			
			$scope.initCompanyPage = function(){
				AuthenticationService.isUserLoggedIn()
				.then(function(response){
					console.log('Inside CompanyController.isUserLoggedIn Response :'+response.status);
					AuthenticationService.getUserProfile()
					.then(function(profileResponse){
						var profile = profileResponse.data;
						$scope.userName = profile.name;
						$scope.emailId = profile.emailId;
						$scope.profilePic = profile.profilePic;
						$scope.userId = profile.userId;
						
						if(profile.status == 'COMPANY_NOT_LINKED'){
							$window.location.href = "/SociallBox/eo/home#/company/new";
						}
					});
					
				})
				.catch(function(response){
					console.log('Inside CompanyController.isUserLoggedIn Response :'+response.status);
					$window.location.href = "/SociallBox/eo/login";
				});
			};
			
			$scope.initCompanyProfile = function(){
				AuthenticationService.getUserProfile()
				.then(function(profileResponse){
					var profile = profileResponse.data;
					$scope.userName = profile.name;
					$scope.emailId = profile.emailId;
					$scope.profilePic = profile.profilePic;
					$scope.userId = profile.userId;
					$scope.companyProfile = profile.companyProfile;
					$("#company-profile-div").removeClass("loader");
				});
			};
			
			$scope.createCompany = function(){
				$("#company_process_div").addClass("loader");
				AuthenticationService.getUserProfile()
				.then(function(profileResponse){
					var profile = profileResponse.data;
					
					var userId = profile.userId;
					
					var companyName = $scope.companyName;
					var street = $scope.street;
					var city  = $scope.city;
					var state = $scope.state;
					var zipcode = $scope.zip;
					var country = $scope.country;
					var phone1 = $scope.phone1;
					var phone2 = $scope.phone2;
					var phone3 = $scope.phone3;
					var emailId = $scope.companyEmail;
					var website = $scope.website;
					if(phone2==null){
						phone2 = '';
					}
					if(phone3 == null){
						phone3 = '';
					}
					var createCompanyRequest = ' { '+
											'"event_organizer" : { 		"name" 			: "' +companyName+ 	'",'+
																	'	"email_id" 		: "' + emailId+		'", '+
																	'	"website" 		: "' + website+		'", '+
																	'	"address" 		: {'+
																	'						"street" 	: "' +street+'" ,'+
																	'						"city" 		: "' +city+'" ,'+
																	'						"state" 	: "' +state+'" ,'+
																	'						"zip_code"	: "' +zipcode+'" ,'+
																	'						"country" 	: "' +country+'" '+
																	'				 	  },'+
																	'	"phone1" 		: "' +phone1 + '",'+
																	'	"phone2" 		: "' +phone2 + '", '+
																	'	"phone3" 		: "' +phone3 + '"  '+
																	
																' },'+
											' "is_existing_company" : false ' +
									'}';
				
					
					CompanyService.createCompanyProfile(userId,createCompanyRequest)
					.then(function(createResponse){
						var companyId = createResponse.data.data.company_profile.id;
						
						var coverPic = $scope.coverPic;
				        console.log('Cover Pic :' );
				        console.dir(coverPic);
				        var picType = 'coverPic';
				        if(coverPic != null){
					        CompanyService.uploadCompanyPhoto(userId,companyId,coverPic,picType)
					        .then(function(uploadResponse){
					        	if(uploadResponse.status == 201){
						        	console.log('Uploaded Cover Pic');
						        	
					        	}
					        	AuthenticationService.setUserProfile(createResponse.data.data.id,createResponse.data.data.name,
			        					createResponse.data.data.email_id,createResponse.data.data.profile_id,
			        					createResponse.data.data.profile_pic,createResponse.data.data.status,
			        					profile.password,createResponse.data.data.company_profile)
					        	.then(function(){
					        		$window.location.href = "/SociallBox/eo/company";
					        	});
					        	/*var profilePic = $scope.profilePic;
						        console.log('Profile Pic :' );
						        console.dir(profilePic);
						        
						        if(profilePic != null){
						        	picType = 'profilePic';
						        	 CompanyService.uploadCompanyPhoto(userId,companyId,profilePic,picType)
								        .then(function(uploadResponse){
								        	console.log('Uploaded Profile Pic')
								        })
								        .catch(function(uploadResponse){
											console.log('Error in upload company profile photo. Response :'+uploadResponse.status);
										});
						        }*/
					        })
					        .catch(function(uploadResponse){
								console.log('Error in upload company photo. Response :'+uploadResponse.status);
							});
				        }else{
				        	AuthenticationService.setUserProfile(createResponse.data.data.id,createResponse.data.data.name,
		        					createResponse.data.data.email_id,createResponse.data.data.profile_id,
		        					createResponse.data.data.profile_pic,createResponse.data.data.status,
		        					profile.password,createResponse.data.data.company_profile)
				        	.then(function(){
				        		$window.location.href = "/SociallBox/eo/company";
				        		
				        	});
				        }
				        
				        
					})
					.catch(function(createResponse){
						console.log('Error in creating company profile . Response :'+createResponse.status);
					});
				})
				.catch(function(response){
					console.log('Inside CompanyController.createCompany to get user profile. Response :'+response.status);
					$window.location.href = "/SociallBox/eo/login";
				});
			};
			
		}
		]);