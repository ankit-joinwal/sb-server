'use strict';

angular.module('iLocal')

.controller('fbContoller', ['$scope', '$rootScope','$facebook','AuthenticationService', function($scope, $rootScope,$facebook,AuthenticationService) {
	
    $scope.$on('fb.auth.authResponseChange', function() {
      $rootScope.loginStatus = $facebook.isConnected();
	  console.log('login status '+$rootScope.loginStatus );
	  
      if($rootScope.loginStatus) {
		
		 
        $facebook.api('/me?fields=id,name,email').then(function(user) {
			console.log('me:'+JSON.stringify(user));
			$rootScope.user = user;
			var id = user.id;
			var email = user.email;
			var name = user.name;
			var socialSystem = "FACEBOOK";
			var userSocialDetail = id;
			var socialDetailType = "USER_EXTERNAL_ID";
			AuthenticationService.signup(name,id,email,socialSystem,userSocialDetail,socialDetailType, function(response){
				 if (response.status==201) {
					 console.log('signup sucesfull');
					 
				 }
			});
			
			$facebook.api(user.id+'/picture').then(function(response){
				console.log('User picture url :'+response.data.url);
				$rootScope.userPicture = response.data.url;
			});
			
        });
		
		
      }
    });

    $scope.loginToggle = function() {
      if($rootScope.loginStatus) {
        $facebook.logout();
      } else {
        $facebook.login();
      }
    };

    
  }]);