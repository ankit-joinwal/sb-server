'use strict';

angular.module('Home')

.factory('LocationService',
	['$http','$cookieStore','$rootScope','$q',function($http, $cookieStore,$rootScope,$q) {
		
		var service = {};
		
		service.getLocationInfo = function(successFunc , errorFunc ){
			var deferred = $q.defer();
			 if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(successFunc, errorFunc);
			}
            else {
				 deferred.reject();
				 return deferred.promise;
            }
			deferred.resolve();
			return deferred.promise;
		};
		
		service.storeUserLocInCookies = function(lat,lng,locality){
			
			console.log('Inside LocationService to store user location');
			 $rootScope.userLoc = {};
            $cookieStore.remove('userLoc');
			
			$rootScope.userLoc = {
               
                    lat: lat,
                    lng: lng,
					locality : locality
                
            };
			 $cookieStore.put('userLoc', $rootScope.userLoc);
			
		};
		
		service.cnvrtCordToAddress = function(lat,lng,callback){
			
			var latlng = new google.maps.LatLng(lat, lng);
			var geocoder = new google.maps.Geocoder();
			var response = {};
			console.log('Inside cnvrtCordToAddress : lat = '+lat + ' , lng = '+lng);
			geocoder.geocode({latLng: latlng}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
				  if (results[1]) {
					var arrAddress = results;
					var locality;
					var areaInCity;
					console.log(results);
					$(results).each(function(idx, obj){ 
						if(obj.types[1]){
							if(obj.types[1]=="sublocality"){
								console.log("User Location is  : "+obj.formatted_address);
								locality = obj.formatted_address;
							}
						}
					});
					response.status = 200;
					response.data = locality;
					callback(response);
				  } else {
					
					response.status = 400;
					response.data = "No Locality Found";
					callback(response);
				  }
				} else {
				  console.log("Geocoder failed due to: " + status);
					response.status = 400;
					response.data = "No Locality Found";
					callback(response);
				}
			});
			
			
		};
		
		service.cnvrtCordToAddressComponents = function(lat,lng,callback){
			var latlng = new google.maps.LatLng(lat, lng);
			var geocoder = new google.maps.Geocoder();
			var response = {};
			geocoder.geocode({latLng: latlng}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
				
					if (results[1]) {
						response.status = 200;
						response.data = results[1].address_components;
						callback(response);
					}else{
						response.status = 200;
						response.data = results[0].address_components;
						callback(response);
					}
				 
				} else {
				  console.log("Geocoder failed due to: " + status);
					response.status = 400;
					response.data = "No Locality Found";
					callback(response);
				}
			});
		};
		
		service.getUserLocation = function(){
			var deferred = $q.defer();
			
			console.log('Inside LocationService to get user location');
			var response = {};
			var userLoc = $cookieStore.get('userLoc') || {};
			console.log('User Location : '+userLoc.locality);
			response.status = 200;
			response.data = userLoc;
			
			deferred.resolve(response);
			return deferred.promise;
		};
		
	
		 return service;
	}
	]);