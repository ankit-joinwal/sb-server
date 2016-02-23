'use strict';

angular.module('Home')


.factory('PlacesService',
	['$http','LocationService',function($http,LocationService){
		var service = {};
		
		service.searchNearby = function(categoryId,lat,lng,callback) {
		
			var location = lat+','+lng;
			
			console.log('Inside PlacesService.searchNearby. User location :'+location);
			$http({
				method:'GET',
				url: '/GeoService/api/public/places/nearby?cid='+categoryId+ '&location='+location,
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   console.log('Search Data for searchNearby : '+categoryId+' = '+response.data.results);
			  callback(response);
			});
		};
		
		service.textSearch = function(query,callback) {
			
			console.log('Inside PlacesService.textSearch');
			$http({
				method:'GET',
				url: '/GeoService/api/public/places/tsearch?query='+query,
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   console.log('Search Data for text search  : '+query+' = '+response.data.results);
			  callback(response);
			});
		};
		
		service.placeDetail = function(referenceId,callback) {
			
			console.log('Inside PlacesService.placeDetail');
			$http({
				method:'GET',
				url: '/GeoService/api/public/places/place/'+referenceId+'/detail',
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   console.log('Search Data for text search  : '+referenceId+' = '+response.data);
			  callback(response);
			});
		};
		
		
		
		 return service;
	
	}
	]);