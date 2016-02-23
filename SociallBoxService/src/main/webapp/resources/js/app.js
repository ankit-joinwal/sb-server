'use strict';

// declare modules
angular.module('Authentication', []);
angular.module('Home', []);

angular.module('iLocal', [
    'Authentication',
    'Home',
    'ngRoute',
    'ngCookies','ui.bootstrap','ngFacebook'
])

.config(['$routeProvider','$facebookProvider', function ($routeProvider,$facebookProvider) {
	$facebookProvider.setAppId('958953034154475').setPermissions(['public_profile','email','user_friends']);
	 
    $routeProvider
       

        .when('/', {
            controller: 'HomeController',
            templateUrl: '/GeoService/resources/public/home.html'
        })
		.when('/rd/meetups/:uri', {
            controller: 'LoginController',
            templateUrl: '/GeoService/resources/public/home.html'
        })
		.when('/rd/events/:uri', {
            controller: 'LoginController',
            templateUrl: '/GeoService/resources/public/home.html'
        })
		.when('/categories/:categoryId/:categoryDesc', {
			templateUrl: '/GeoService/resources/public/partials/catDetails.html',
			controller: 'CatDetailsController'
		})
		.when('/categories/:categoryId/:categoryDesc/places', {
			templateUrl: '/GeoService/resources/public/partials/places.html',
			controller: 'NearbySearchController'
		})
		.when('/categories/events', {
			templateUrl: '/GeoService/resources/public/partials/eventsHome.html',
			controller: 'EventsController'
		})
		.when('/places/:query', {
			templateUrl: '/GeoService/resources/public/partials/places.html',
			controller: 'TextSearchController'
		})
		.when('/places/place/:referenceId', {
			templateUrl: '/GeoService/resources/public/partials/place.html',
			controller: 'PlacesController'
		})
		.when('/meetups/create', {
			templateUrl: '/GeoService/resources/public/partials/createMeetup.html',
			controller: 'MeetupsController'
		})
		.when('/meetups/:meetupId', {
			templateUrl: '/GeoService/resources/public/partials/editMeetup.html',
			controller: 'MeetupsController'
		})
		.when('/events/create', {
			templateUrl: '/GeoService/resources/public/partials/createEvent.html',
			controller: 'EventsController'
		})
		.when('/events/:eventId', {
			templateUrl: '/GeoService/resources/public/partials/viewEvent.html',
			controller: 'EventsController'
		})
		.when('/events/:eventId/edit', {
			templateUrl: '/GeoService/resources/public/partials/editEvent.html',
			controller: 'EventsController'
		})
		.otherwise({ redirectTo: '/' });
}])

.run(['$rootScope', '$window','$location', '$cookieStore', '$facebook','$http','AuthenticationService',
    function ($rootScope, $window, $location, $cookieStore,$facebook, $http,AuthenticationService) {
		console.log('Current location : ' +$location.path());
		
        // keep user logged in after page refresh
		/*
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }
        });
        */
		//Initialize Facebook SDK
		(function(d, s, id) {
		  var js, fjs = d.getElementsByTagName(s)[0];
		  if (d.getElementById(id)) return;
		  js = d.createElement(s); js.id = id;
		  js.src = "//connect.facebook.net/en_US/sdk.js";
		  fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));
		
		$rootScope.$on('fb.load', function() {
		  $window.dispatchEvent(new Event('fb.load'));
		});
		/*
		$rootScope.$on('fb.auth.authResponseChange', function() {
			console.log('App.js run :');
			
		});*/
    }]);
