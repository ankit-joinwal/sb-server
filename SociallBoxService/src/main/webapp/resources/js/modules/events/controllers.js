'use strict';

var app = angular.module('Events',['textAngular']);

app.controller('EventsController',
		['$window','$scope', '$rootScope','$cookieStore', '$routeParams','$location','AuthenticationService','EventService',
        function ($window,$scope, $rootScope, $routeParams,$location,AuthenticationService,EventService) {
		
		   $scope.eventDetails = '<h2>Enter event details like features,ticket booking url etc</h2>';
			
			$scope.initNewEvent = function(){
				$window.location.href = "/SociallBox/eo/home#/events/new";
			};
			
			$scope.myevents_upcoming = function (){
				AuthenticationService.isUserLoggedIn()
				.then(function(response){
					EventService.myevents_upcoming()
					.then(function(eventResponse){
						$scope.myEventsData_upcoming =eventResponse.data.data;
						});
					
				}).catch(function(response){
					console.log("Inside event service controller Response :"+response.status);
//					$window.location.href = "/SociallBox/eo/login";
		
				});
			}
			
			$scope.myevents_past = function (){
				AuthenticationService.isUserLoggedIn()
				.then(function(response){
					EventService.myevents_past()
					.then(function(eventResponse){
						$scope.myEventsData_past =eventResponse.data.data;
						});
					
				}).catch(function(response){
					console.log("Inside event service controller Response :"+response.status);
//					$window.location.href = "/SociallBox/eo/login";
		
				});
			}
			
			
			
			//$scope.tagNames = [];
			var tags = [],isFree,startDate,endDate;
			
			$scope.uploadDataForReview =function(){
				if($('#IsFreeButton button.active').text()=="YES")
					isFree ="true";
					else
						isFree="false";
				var tagName ={};
				$("#tags button").each(function(){
					var buttonClass =$(this).attr("class");
					if (buttonClass == "btn btnactive"){
						var tagText = $(this).text();
						tagName = { 
								"name" : tagText,
							};
						tags.push(tagName);
					};
				});
				
					 startDate =$("#startDate").val();
					 endDate = $("#endDate").val();
					
				   $("#reviewEventStartDate").text(startDate);
				   $("#reviewEventEndDate").text(endDate);
				   
				   for(var i =0;i<tags.length;i++){
						$(".reviewTagNames").append('<button type="button" class="btn btnactive">'+tags[i].name+'</button>');
						
					};
					if (isFree == "true")
					$(".reviewFreeButton").append('<button type="button" class="btn btnactive">Yes</button>');
					else
					$(".reviewFreeButton").append('<button type="button" class="btn btnactive">NO</button>');
					
			}; 
			
			$scope.createEvent = function(){
				AuthenticationService.isUserLoggedIn()
				.then(function(response){
					var eventDetails = $scope.eventDetails;
					var eventTitle =$scope.eventTitle;
					startDate =$("#startDate").val();
					endDate = $("#endDate").val();
				//	var profileId = $routeParams.profileId;
					var locationName = $scope.eventLocation;
					var locLat = $scope.event_place_lat;
					var locLng = $scope.event_place_lng;
					var locality = $scope.event_address_components[1].short_name;
					console.log('Name :'+locationName);
					console.log('Lat :'+locLat);
					console.log('Long :'+locLng);
					console.log('shortName' + shortName);
				
					
					var createEventRequest = ' { '+
											 ' 	"title" 		 : "' +eventTitle+ 	'",'+
											 '	"description" 	 : "' + eventDetails+		'", '+
											 '	"event_details"  : {'+
																	' "location"		: {'+
																	'						"name" 	: "' +locationName+'" ,'+
																	'						"locality" 		: "' +locality+'" ,'+
																	'						"longitude" 	: "' +locLat+'" ,'+
																	'						"lattitude"	: "' +locLng+'" ,'+
																	'				 	  },'+
																	' },'+
											'	"tags" 		     : "' +tags + '",'+
											'	"startDate" 	 : "' +startDate + '", '+
											'	"endDate" 		 : "' +endDate + '" , '+
											'	"profileId" 	 : "' +profileId + '" , '+
											'   "is_free"        : "' +isFree + '"  '+
									'}';
					
					
					EventService.createEvent(createEventRequest)
					.then(function(eventResponse){
						var eventId = eventResponse.data.id;
						var eventPic = $scope.eventPic;
				        console.log('event Pic :' );
				        console.dir(eventPic);
				        if(eventPic != null){
				        	EventService.uploadEventPhoto(eventId,eventPic)
					        .then(function(uploadResponse){
					        	if(uploadResponse.status == 201){
						        	console.log('Uploaded event Pic');
						        	
					        	}
					         })
					        .catch(function(uploadResponse){
								console.log('Error in uploading event photo. Response :'+uploadResponse.status);
							});
				        }
					})
					.catch(function(eventResponse){
						console.log('Error in creating company profile . Response :'+createResponse.status);
					});
				})
				.catch(function(response){
					console.log('Inside EventController.createEvent to create event. Response :'+response.status);
					//console.log('Inside EventController.createEvent to create event. Response :'+response.status);
					//$window.location.href = "/SociallBox/eo/login";
				});
			};
			
			
		}
		
		]);