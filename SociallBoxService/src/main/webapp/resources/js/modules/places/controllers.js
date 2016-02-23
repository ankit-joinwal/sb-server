'use strict';

var app = angular.module('Home');

app.controller('NearbySearchController',
    ['$scope',"$http",'$routeParams','PlacesService','AuthenticationService','LocationService',
     function ($scope,$http,$routeParams,PlacesService,AuthenticationService,LocationService) {
    	 $scope.curPage = 0;
		 $scope.pageSize = 6;
    	var categoryId = $routeParams.categoryId;
		var catDesc = $routeParams.categoryDesc;
		
		var lng,lat;
		var userLoc = LocationService.getUserLocation().then(function(response){
			if(response.status == 200){
				lat = response.data.lat;
				lng = response.data.lng;
				PlacesService.searchNearby(categoryId,lat,lng,function(response){
					console.log('Inside callback of PlacesController. Response status '+response.status);
					if (response.status==200) {
						console.log('Inside PlacesController ... Recieved success from Places service');
						$scope.searchInfo = catDesc+" ("+response.data.totalRecords+") :";
						$scope.places = response.data.results;
						
						
						$scope.numberOfPages = function() {
							return Math.ceil($scope.places.length / $scope.pageSize);
						};
						
						
						/*---------------------------------------------
						*	start creating map 
						* ---------------------------------------------
						*/
						var mapCenter = { "lat":lat, "lng":lng };
						var placesArr = response.data.results;
						var mapDiv = 'map';
						console.log('inside NearbySearchController. Going to create map');
						
						console.log('Inside Create Map');
						console.log('MapCenter.lat = '+mapCenter.lat +' ,long = '+mapCenter.lng);
						console.log('Initialising map options');
						 var mapOptions = {
							  zoom: 14,
							  center: new google.maps.LatLng(mapCenter.lat,mapCenter.lng),
							  mapTypeId: google.maps.MapTypeId.TERRAIN
						  };
						  console.log('Creating map object ');
						  $scope.map = new google.maps.Map(document.getElementById(mapDiv), mapOptions);
						  $scope.markers = [];
						  var infoWindow = new google.maps.InfoWindow();
						  
						   var createMarker = function (info){
							  
							  var marker = new google.maps.Marker({
								  map: $scope.map,
								  position: new google.maps.LatLng(info.geometry.location.lat, info.geometry.location.lng),
								  animation: google.maps.Animation.DROP,
								  label:info.name,
								  title: info.name
							  });
							 marker.setMap($scope.map);
							  
						  } ;
						  console.log('Initialising markers ');
						  for (var i = 0; i < placesArr.length; i++){
							  createMarker(placesArr[i]);
						  }
						 
						console.log('Map created successfully ');
						/*---------------------------------------------
						*	end creating map 
						* ---------------------------------------------
						*/
					}else{
						console.log('Inside PlacesController ... Recieved failure from Places service');
						alert("Unable to get search data ");
					}
				});
			}
		});
		
		
		
    }])
.filter('pagination', function()
{
 return function(input, start)
 {
	if (!input || !input.length) { return; }
	  start = +start;
	  return input.slice(start);
 };
});

app.controller('TextSearchController',
    ['$scope',"$http",'$routeParams','PlacesService',
     function ($scope,$http,$routeParams,PlacesService) {
    	 $scope.curPage = 0;
		 $scope.pageSize = 6;
		var query = $routeParams.query;
			console.log('Inside TextSearchController. Query Recieved '+query);
			PlacesService.textSearch(query,function(response){
				console.log('Inside callback of TextSearchController.tsearch. Response status '+response.status);
			if (response.status==200) {
				console.log('Inside TextSearchController ... Recieved success from Places service');
				var queryFormatted = query.split('+').join(' ');
				$scope.searchInfo = queryFormatted+" ("+response.data.totalRecords+"):";
				$scope.places = response.data.results;
				$scope.numberOfPages = function() {
					return Math.ceil($scope.places.length / $scope.pageSize);
				};
				
				/*---------------------------------------------
				*	start creating map 
				* ---------------------------------------------
				*/
				
				var placesArr = response.data.results;
				var mapDiv = 'map';
				var mapCenterLat = placesArr[0].geometry.location.lat;
				var mapCenterLng = placesArr[0].geometry.location.lng;
				console.log('inside NearbySearchController. Going to create map');
				
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
						  position: new google.maps.LatLng(info.geometry.location.lat, info.geometry.location.lng),
						  animation: google.maps.Animation.DROP,
						  label:info.name,
						  title: info.name
					  });
					 marker.setMap($scope.map);
					  
				  } ;
				  console.log('Initialising markers ');
				  for (var i = 0; i < placesArr.length; i++){
					  createMarker(placesArr[i]);
				  }
				 
				console.log('Map created successfully ');
				/*---------------------------------------------
				*	end creating map 
				* ---------------------------------------------
				*/
				
			}else{
				console.log('Inside TextSearchController ... Recieved failure from Places service');
				alert("Unable to get search data ");
			}
			});
		
    }])
.filter('pagination', function()
{
 return function(input, start)
 {
	if (!input || !input.length) { return; }
	  start = +start;
	  return input.slice(start);
 };
});

app.controller('PlacesController',
    ['$scope',"$http",'$routeParams','$window', '$location','PlacesService','AuthenticationService',
     function ($scope,$http,$routeParams,$window,$location,PlacesService,AuthenticationService) {
			$scope.curPage = 0;
			$scope.pageSize = 6;
			var reference = $routeParams.referenceId;
			console.log('Inside PlacesController. ReferenceId Recieved '+reference);
			
			
			PlacesService.placeDetail(reference,function(response){
				console.log('Inside callback of PlacesController.tsearch. Response status '+response.status);
			if (response.status==200) {
				console.log('Inside PlacesController ... Recieved success from Places service');
				console.log('Response data : '+response.data.result);
				$scope.placeInfo = response.data.result;
				$scope.placeInfo.rating = 3;
				
				
				/*---------------------------------------------
				*	start creating map 
				* ---------------------------------------------
				*/
				
				var placeDetail = response.data.result;
				var mapDiv = 'map';
				var mapCenterLat = placeDetail.geometry.location.lat;
				var mapCenterLng = placeDetail.geometry.location.lng;
				console.log('inside NearbySearchController. Going to create map');
				
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
						  position: new google.maps.LatLng(info.geometry.location.lat, info.geometry.location.lng),
						  animation: google.maps.Animation.DROP,
						  label:info.name,
						  title: info.name
					  });
					 marker.setMap($scope.map);
					  
				  } ;
				  console.log('Initialising markers ');
				  
					createMarker(placeDetail);
				 
				console.log('Map created successfully ');
				/*---------------------------------------------
				*	end creating map 
				* ---------------------------------------------
				*/
				$scope.placeGeometry = {"name":placeDetail.formatted_address, "lat":mapCenterLat ,"lng":mapCenterLng};
			}else{
				console.log('Inside PlacesController ... Recieved failure from Places service');
				alert("Unable to get search data ");
			}
			});
			
			$scope.open_now = true;
			$scope.share = function(){
				var isUserLogIn = true;
				AuthenticationService.isUserLoggedIn(function(loginCheckResponse){
					if(loginCheckResponse.status != 200){
						console.log('PlacesController.share : User not logged in.');
						isUserLogIn = false;
					}
				});
				if(!isUserLogIn){
					alert('Please login to share');
				}
				
				var url = encodeURI($window.location);
				console.log('$window.location ='+url);
				$window.FB.ui({
				  method: 'share',
				  href: url,
				}, function(response){
					
				});
			};
    }]);