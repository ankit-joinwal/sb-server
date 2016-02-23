'use strict';

angular.module('Home')

.factory('CategoryService',
	['$http',function($http){
		
		var service = {};
		service.rootCategories = function(callback){
				console.log('Inside Category Service');
				$http({
					method:'GET',
					url: '/GeoService/api/public/categories',
					headers: {
							"X-Login-Ajax-call": 'true',
							"Accept" : "application/json"
					}
				}).then(function(response) {
				   console.log('Category data : '+response.data);
				  callback(response);
				});
		};
		
		service.subCategories = function(categoryId,callback){
			console.log('Inside Category Service.subCategories');
			$http({
				method:'GET',
				url: '/GeoService/api/public/categories/'+categoryId+"/subcategories",
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   console.log('Sub Category data : '+response.data);
			  callback(response);
			});
		};
		
		service.allSubCategories = function(callback){
			console.log('Inside Category Service.subCategories');
			$http({
				method:'GET',
				url: '/GeoService/api/public/categories/subcategories',
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   console.log('Sub Category data : '+response.data);
			  callback(response);
			});
		};
	
		 return service;
	}
	]);