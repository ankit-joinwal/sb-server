'use strict';

var app = angular.module('Events');

app.factory('EventService',
	    [ '$http', '$cookieStore', '$rootScope','$q','AuthenticationService',
	    function ($http, $cookieStore, $rootScope, $q,AuthenticationService) {
	    	var service = {};
	    	
	    	return service;
	    }
	    ]);
	    	