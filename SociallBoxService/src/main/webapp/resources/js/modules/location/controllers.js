'use strict';

var app = angular.module('Home');


  
  app.controller('LocationController',
    ['$location','$rootScope','$scope',"$http",'LocationService',
    function ($location,$rootScope,$scope,$http,LocationService) {
		console.log('Inside LocationController');
    	$scope.auto_location_error = "";
		$scope.showPosition = function (position) {
            $scope.user_lat = position.coords.latitude;
            $scope.user_lng = position.coords.longitude;
			console.log('position:'+position);
				var user_lat= $scope.user_lat;
				var user_lng = $scope.user_lng;
				
				LocationService.cnvrtCordToAddress(user_lat, user_lng , function (response) {
					console.log('Inside callback of LocationController. Response status '+response.status);
					if (response.status==200) {
						console.log('Inside LocationController ... Recieved success from Location Service');
						$scope.locality = response.data;
						$scope.userLocFound = true;
						LocationService.storeUserLocInCookies($scope.user_lat,$scope.user_lng,$scope.locality );
					} else {
						console.log('Inside LocationController ... Recieved failure from Location Service');
						$scope.auto_location_error = response.data ;
						$scope.userLocFound = false;
					}
					$scope.$apply();
				});
        };
		
		$scope.showError = function (error) {
            switch (error.code) {
                case error.PERMISSION_DENIED:
                    $scope.auto_location_error = "User denied the request for Geolocation.";
                    break;
                case error.POSITION_UNAVAILABLE:
                    $scope.auto_location_error = "Location information is unavailable.";
                    break;
                case error.TIMEOUT:
                    $scope.auto_location_error = "The request to get user location timed out.";
                    break;
                case error.UNKNOWN_ERROR:
                    $scope.auto_location_error = "An unknown error occurred.";
                    break;
            }
            $scope.$apply();
        };
		
		 $scope.getLocation = function () {
			$scope.locality="Fetching location...";
			LocationService.getLocationInfo($scope.showPosition , $scope.showError).then(function(){
				
				
			});
        };
		
		
		
	   
		
	   
}]);

app.directive('googleplace', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, model) {
            var options = {
                types: [],
                componentRestrictions: {}
            };
            scope.gPlace = new google.maps.places.Autocomplete(element[0], options);

            google.maps.event.addListener(scope.gPlace, 'place_changed', function() {
				 var geoComponents = scope.gPlace.getPlace();
                var latitude = geoComponents.geometry.location.lat();
                var longitude = geoComponents.geometry.location.lng();
				console.log('Location Lat '+latitude+' , lng '+longitude);
				
				scope.meetup_address_components = geoComponents.address_components;
				scope.event_address_components = geoComponents.address_components;
				scope.meetup_place_lat = latitude;
				scope.meetup_place_lng = longitude;
				scope.event_place_lat = latitude;
				scope.event_place_lng = longitude;
                scope.$apply(function() {
                    model.$setViewValue(element.val());                
                });
            });
        }
    };
});