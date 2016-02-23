'use strict';

angular.module('Home')

.controller('EventsController',
    ['$rootScope','$scope',"$http",'$routeParams','$location','$window','$facebook','EventService','AuthenticationService',
    function ($rootScope,$scope,$http,$routeParams,$location,$window,$facebook,EventService,AuthenticationService) {
	
		//Function to navigate to CreateEvent page
		$scope.newEvent = function(){
			console.log('Inside EventsController.newEvent');
			$location.path('/events/create');
		};
		
		$scope.getPersonalizedEvents = function(){
			var userProfile = {};
			AuthenticationService.getUserProfile(function (response){
				if(response.status == 200){
					userProfile = response.data;
					var id ;
					if(typeof userProfile !== 'undefined'){
						id = userProfile.id;
					}else{
						id = null;
					}
					
					EventService.getEventsForYou(id,'delhi','india',function(eventResponse){
							if(eventResponse.status == 200){
								$scope.personalizedEvents = eventResponse.data.events;
							}else{
								alert('No Events found');
							}
						});
				}else{
					console.log('Unable to get User Profile from cookies');
				}
				
			});
			
			
		};
		
		$scope.userTags = function(){
			$scope.loginStatus = $facebook.isConnected();
			if(!$scope.loginStatus){
			  //$facebook.login();
			  alert('Please login via facebook');
			  return;
			}
			
			$('#tags').tagsinput({
			  itemValue: 'name',
			  itemText: 'description'
			  
			  
			});

			var userProfile = {};
			AuthenticationService.getUserProfile(function (response){
				if(response.status == 200){
					userProfile = response.data;
				}else{
					console.log('Unable to get User Profile from cookies');
				}
				
			});
			var userId = userProfile.id;
			console.log('User id '+userId);
			EventService.getUserTags(userId,function(response){
				if(response.status == 200){
					var tags = response.data;
					$.each(tags, function(i, item) {
						$('#tags').tagsinput('add', item);
					});
						
				}
			});
			$('#tagsModal').modal('show') ;
		};
		
		$scope.saveUserTags = function(){
			var tags = angular.toJson($("#tags").tagsinput('items'));
			var userProfile = {};
			AuthenticationService.getUserProfile(function (response){
				if(response.status == 200){
					userProfile = response.data;
				}else{
					console.log('Unable to get User Profile from cookies');
				}
				
			});
			var userId = userProfile.id;
			console.log('User id '+userId);
			EventService.saveUserTags(userId,tags,function(response){
				if(response.status == 200){
					$('#tagsModal').modal('hide')
					alert('Save tags successfull');
				}
			});
		};
		
		$scope.initEventTypes = function(){
			EventService.getEventTypes(function(response){
				if(response.status == 200){
					$scope.eventTypes = response.data;
					$scope.isCategorySelected = false;
					
				}else{
					alert('No eventTypes found');
				}
			});
		};
		
		$scope.searchByCategory = function($event, name) {
			console.log('Inside searchByCategory for '+name);
			EventService.getEventsByEventType(name,'delhi','india',function(response){
				if(response.status == 200){
					console.log('Total events Found :'+response.data.count);
					$scope.isCategorySelected = true;
					$scope.eventsByType = response.data.events;
				}else{
					alert('No Events found for type '+name);
				}
			});
		};
		
		$scope.backToEventTypes = function(){
			EventService.getEventTypes(function(response){
				if(response.status == 200){
					$scope.eventTypes = response.data;
					$scope.isCategorySelected = false;
					$scope.eventsByType = [];
				}else{
					alert('No eventTypes found');
				}
			});
		};
		
		//Function to initialize CreateEvent page
		$scope.initNewEvent = function(){
			console.log('Inside initNewEvent');
			EventService.getEventTimeSlots(function(response){
			   if(response.status == 200){
				$scope.timeSlots = response.data;   
			   }
			});
		   
		   $('#tags').tagsinput({
			  itemValue: 'name',
			  itemText: 'description'
			  
			  
			});

			EventService.getAllTags(function(response){
				if(response.status == 200){
					var tags = response.data;
					$.each(tags, function(i, item) {
						$('#tags').tagsinput('add', item);
					});
						
				}
			});
			

		 
		   //Setting Max limit on description field
			var left = 500;
			$('#text_counter').text('Characters left: ' + left);
				$('#desc').keyup(function () {
				left = 500 - $(this).val().length;
				if(left < 0){
					$('#text_counter').addClass("overlimit");
				}else{
					$('#text_counter').removeClass("overlimit");
				}
				$('#text_counter').text('Characters left: ' + left);
			});
		};
		
		
		$scope.createEvent = function(){
			$scope.loginStatus = $facebook.isConnected();
			if(!$scope.loginStatus){
			  //$facebook.login();
			  alert('Please login via facebook');
			  return;
			}
			
			var title = $scope.title;
			var desc = $scope.desc;
			var startDate = $('#startDate').val();
			var startTime = $scope.startTime;
			var endDate = $('#endDate').val();
			var endTime = $scope.endTime;
			
			var userProfile = {};
			AuthenticationService.getUserProfile(function (response){
				if(response.status == 200){
					userProfile = response.data;
				}else{
					console.log('Unable to get User Profile from cookies');
				}
				
			});
			//Get Location
			var locationName = $scope.eventPlace;
			var locLat = $scope.event_place_lat;
			var locLng = $scope.event_place_lng;
			var addressComponents = JSON.stringify($scope.event_address_components);
			var location = '{"name": "'+locationName + '" ,"longitude" :"'+locLng+'" ,"lattitude" : "'+locLat+'"}';
			
			var tags = angular.toJson($("#tags").tagsinput('items'));
			
			console.log('Title:'+title);
			console.log('Description:'+desc);
			console.log('Location Name :'+locationName);
			console.log('Location Lat:'+locLat);
			console.log('Location Lng:'+locLng);
			console.log('User Profile :'+JSON.stringify(userProfile));
			console.log('StatDate:'+startDate);
			console.log('startTime:'+startTime);
			console.log('endDate:'+endDate);
			console.log('endTime:'+endTime);
			console.log('tags:'+tags);
			console.log('addressComponents:'+addressComponents);
			
			EventService.createEvent(title,desc,userProfile.email,location,addressComponents,startDate,startTime,endDate,endTime,tags,function(response){
				if(response.status == 201){
					
					console.log('Create Event Successful');
					console.log('Event Id : '+response.data.uuid);
					$scope.createdEventId = response.data.uuid;
					$scope.uploadFile();
					
					$location.path('events/'+response.data.uuid +'/edit');
				}else{
					console.log('Create Event Failed');
				}
			});
		};
		
		 $scope.uploadFile = function() {
			 
		     $scope.processDropzone();
			 var fd = new FormData();
			 var images = $scope.images;
			 for(var i =0;i<images.length;i++){
					fd.append('file'+i, images[i]);
			 }
			
			
			$http.post('http://ilocal.com:8080/GeoService/api/public/events/'+$scope.createdEventId+'/images/upload', fd, {
				withCredentials: false,
				headers: {
				  'Content-Type': undefined
				},
				transformRequest: angular.identity,
				params: {
				  fd
				}
			  })
			  .success(function(response, status, headers, config) {
				console.log('Files uploaded successfully for event :'+$scope.createdEventId);
			  })
			  .error(function(error, status, headers, config) {
				console.log(error);
			  });
		 };

		 $scope.reset = function() {
		    $scope.resetDropzone();
		 };
		
		$scope.initEditEvent = function(){
			console.log('Inside initEditEvent  ');
			var loginStatus = false;
			AuthenticationService.isUserLoggedIn(function(authStatus){
			   if(authStatus.status == 200){
				   loginStatus = true;
			   }
			});
			
			if(!loginStatus){
				console.log('User not login. Redirecting to login');
				var currLoc = $location.path();
				var newPath = '/rd'+currLoc;
				$location.path(newPath);
			}
			
			 var eventId = $routeParams.eventId;
			 var eventFound = true;
			 EventService.getEvent(eventId,function(response){
				 if(response.status == 200){
					 console.log('Found Event');
					 $scope.eventInfo  = response.data; 
					 var tags = response.data.tags;
					 console.log('Tags :'+tags);
					$('#tags').tagsinput({
						itemValue: 'name',
						itemText: 'description'
					});
					 $.each(tags, function(i, item) {
						$('#tags').tagsinput('add', item);
					 });
					 var mapDiv = 'eventLocMap';
					var mapCenterLat = $scope.eventInfo.eventDetails.location.lattitude;
					var mapCenterLng = $scope.eventInfo.eventDetails.location.longitude;
					console.log('inside EventController. Going to create map');
					
					console.log('Inside Create Map');
					console.log('MapCenter.lat = '+ mapCenterLat+' ,long = '+mapCenterLng);
					console.log('Initialising map options');
					 var mapOptions = {
						  zoom: 14,
						  center: new google.maps.LatLng(mapCenterLat,mapCenterLng),
						  mapTypeId: google.maps.MapTypeId.TERRAIN
					  };
					  console.log('Creating map object ');
					  $scope.map = new google.maps.Map(document.getElementById(mapDiv), mapOptions);
					  $scope.markers = [];
					  var infoWindow = new google.maps.InfoWindow();
					  
					   var createMarker = function (info){
						  
						  var marker = new google.maps.Marker({
							  map: $scope.map,
							  position: new google.maps.LatLng(info.eventDetails.location.lattitude, info.eventDetails.location.longitude),
							  animation: google.maps.Animation.DROP,
							  label:info.name,
							  title: info.name
						  });
						 marker.setMap($scope.map);
						  
					  } ;
					  console.log('Initialising markers ');
					  
						createMarker($scope.eventInfo);
					 
					console.log('Map created successfully ');
					/*---------------------------------------------
					*	end creating map 
					* ---------------------------------------------*/
				 }else{
					 eventFound = false;
					 console.log('Event not found!');
				 }
			 });
			 
			 
		};
		
		$scope.initViewEvent = function(){
			console.log('Inside initEditEvent  ');
			var loginStatus = false;
			AuthenticationService.isUserLoggedIn(function(authStatus){
			   if(authStatus.status == 200){
				   loginStatus = true;
			   }
			});
			
			if(!loginStatus){
				console.log('User not login. Redirecting to login');
				var currLoc = $location.path();
				var newPath = '/rd'+currLoc;
				$location.path(newPath);
			}
			
			 var eventId = $routeParams.eventId;
			 var eventFound = true;
			 EventService.getEvent(eventId,function(response){
				 if(response.status == 200){
					 console.log('Found Event');
					 $scope.eventInfo  = response.data; 
					 var tags = response.data.tags;
					 console.log('Tags :'+tags);
					$('#tags').tagsinput({
						itemValue: 'name',
						itemText: 'description'
					});
					 $.each(tags, function(i, item) {
						$('#tags').tagsinput('add', item);
					 });
					 var mapDiv = 'eventLocMap';
					var mapCenterLat = $scope.eventInfo.eventDetails.location.lattitude;
					var mapCenterLng = $scope.eventInfo.eventDetails.location.longitude;
					console.log('inside EventController. Going to create map');
					
					console.log('Inside Create Map');
					console.log('MapCenter.lat = '+ mapCenterLat+' ,long = '+mapCenterLng);
					console.log('Initialising map options');
					 var mapOptions = {
						  zoom: 14,
						  center: new google.maps.LatLng(mapCenterLat,mapCenterLng),
						  mapTypeId: google.maps.MapTypeId.TERRAIN
					  };
					  console.log('Creating map object ');
					  $scope.map = new google.maps.Map(document.getElementById(mapDiv), mapOptions);
					  $scope.markers = [];
					  var infoWindow = new google.maps.InfoWindow();
					  
					   var createMarker = function (info){
						  
						  var marker = new google.maps.Marker({
							  map: $scope.map,
							  position: new google.maps.LatLng(info.eventDetails.location.lattitude, info.eventDetails.location.longitude),
							  animation: google.maps.Animation.DROP,
							  label:info.name,
							  title: info.name
						  });
						 marker.setMap($scope.map);
						  
					  } ;
					  console.log('Initialising markers ');
					  
						createMarker($scope.eventInfo);
					 
					console.log('Map created successfully ');
					/*---------------------------------------------
					*	end creating map 
					* ---------------------------------------------*/
				 }else{
					 eventFound = false;
					 console.log('Event not found!');
				 }
			 });
			 
			 
		};
		
		$scope.makeLive = function(){
			var eventId = $scope.eventInfo.uuid;
			
			EventService.goLiveEvent(eventId,function(response){
				if(response.status == 200){
					alert("Event Now Live");
				}else{
					alert("Make Event Successfull failed");
				}
			});
		};
		
		
		$scope.openMap = function(){
			var place = $scope.eventPlace;
			var placeLat = $rootScope.event_place_lat;
			var placeLng = $rootScope.event_place_lng;
			if(typeof place !== 'undefined'){
				var url = "http://maps.google.com/?q="+placeLat+","+placeLng;
				$window.open(url);
			}
		};
		
		
		 
	}]);