'use strict';

angular.module('Home')

.controller('CatDetailsController',
    ['$scope',"$http",'$routeParams','CategoryService',
    function ($scope,$http,$routeParams,CategoryService) {
    	var categoryId = $routeParams.categoryId;
		var catDesc = $routeParams.categoryDesc;
	   CategoryService.subCategories(categoryId, function (response) {
			console.log('Inside callback of CatDetailsController. Response status '+response.status);
			if (response.status==200) {
				console.log('Inside CatDetailsController ... Recieved success from Category service');
				
				$scope.categoryId = categoryId;
				$scope.categoryDesc = catDesc;
				$scope.subCategories = response.data;
			} else {
				console.log('Inside CatDetailsController ... Recieved failure from Category service');
				alert("Unable to get Categories");
			}
		});
		
    	
    }]);