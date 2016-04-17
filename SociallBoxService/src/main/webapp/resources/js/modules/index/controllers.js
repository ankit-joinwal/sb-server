'use strict';

var app = angular.module('index');

app.controller('IndexController',
    ['$location','$rootScope','$scope',"$http",
    function ($location,$rootScope,$scope,$http) {
    	console.log("IndexController Called");
    }]);
