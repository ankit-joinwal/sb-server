'use strict';

var app = angular.module('Events',['textAngular']);

app.controller('EventsController',
		['$window','$scope', '$rootScope', '$routeParams','$location','AuthenticationService','EventService',
        function ($window,$scope, $rootScope, $routeParams,$location,AuthenticationService,EventService) {
		
		   $scope.eventDetails = '<h2>Enter event details like features,ticket booking url etc</h2>';
			
			$scope.initNewEvent = function(){
				$window.location.href = "/SociallBox/eo/home#/events/new";
			};
			
			$scope.getDetails = function(){
				var eventDetails = $scope.eventDetails;
				alert('htmlContent : '+eventDetails);
			};
			
			$scope.getEventLocation = function(){
				var locationName = $scope.eventPlace;
				var locLat = $scope.event_place_lat;
				var locLng = $scope.event_place_lng;
				var addressComponents = JSON.stringify($scope.event_address_components);
				console.log('Name :'+locationName);
				console.log('Lat :'+locLat);
				console.log('Long :'+locLng);
				console.log('AddressComponents' + JSON.stringify(JSON.parse(addressComponents),null,2));
			}
		}
		
		]);