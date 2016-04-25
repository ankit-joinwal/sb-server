'use strict';

var app = angular.module('Events');

app.controller('EventsController',
		['$window','$scope', '$rootScope', '$routeParams','$location','AuthenticationService','EventService',
        function ($window,$scope, $rootScope, $routeParams,$location,AuthenticationService,EventService) {
			
			$scope.initNewEvent = function(){
				$window.location.href = "/SociallBox/eo/home#/events/new";
			};
		
		
		}
		
		]);