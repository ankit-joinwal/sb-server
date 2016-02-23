'use strict';

angular.module('Authentication')

.factory('AuthenticationService',
    ['Base64', '$http', '$cookieStore', '$rootScope', '$facebook','$q',
    function (Base64, $http, $cookieStore, $rootScope, $facebook,$q) {
        var service = {};

       
		service.isUserLoggedIn = function(callback){
			var response = {};
			
			service.getUserProfile(function(userProfileResponse){
				if(userProfileResponse.status == 200 && (typeof userProfileResponse.data !== 'undefined')){
					response.status = 200;
				}else{
					response.status = 403;
				}
			});
			callback(response);
		};
		
		service.promptUserToLogin = function(source){
			var deferred = $q.defer();
			console.log('Inside AuthenticationService.promptUserToLogin');
			var signupResponse = {"status":403};
			if(source=="FACEBOOK"){
				 return $facebook.login().then(function() {
					$rootScope.loginStatus = $facebook.isConnected();
					console.log('login status '+$rootScope.loginStatus );
					
					if($rootScope.loginStatus) {
						return $facebook.api('/me?fields=id,name,email').then(function(user) {
							console.log('me:'+JSON.stringify(user));
							$rootScope.user = user;
							var id = user.id;
							var email = user.email;
							var name = user.name;
							var socialSystem = "FACEBOOK";
							var userSocialDetail = id;
							var socialDetailType = "USER_EXTERNAL_ID";
							return service.signin(name,id,email,socialSystem,userSocialDetail,socialDetailType).then(function(response){
								 if (response.status==201) {
									 console.log('signin sucesfull');
									return  $facebook.api(user.id+'/picture').then(function(response){
										console.log('User picture url :'+response.data.url);
										$rootScope.userPicture = response.data.url;
										
										signupResponse.status = 200;
										deferred.resolve(signupResponse);
										return deferred.promise;
									});
								 }
							});

							
							
						});
						
					}
					
				});
				
			}
			
		};
		
		service.logout = function(source,callback){
			
			if(source=="FACEBOOK"){
				$facebook.logout();
				$rootScope.loginStatus = false;
				service.clearProfile();
				callback();
			}
		};
	   
		service.signin = function(username,socialId,email,socialSystem,socialDetail,socialDetailType){
			var deferred = $q.defer();

			console.log('Inside AuthenticationService--- username = '+username + ' , email = '+email );
        	var postData = '{ "name"	: "'	+username+	'" , 	'+
        					' "emailId" : "'	+email+		'" , 	'+
        					' "password": "'	+socialId+	'" , 	'+
        					' "social_details" : [{ 					'+
        					'						"system" : "'	+socialSystem+ '" ,'+
        					'						"detail" : "'	+socialDetail+ '" ,'+
        					'						"detailType" : "'	+socialDetailType+ '" '+
        					'					}], '+
        					' "isEnabled":"true" '+
        					'}';
			
			return $http({
				method:'POST',
				url: '/GeoService/api/public/users',
	            data: postData,
	            headers: {
	                    "Content-Type": "application/json",
						"accept":"application/json",
	                    "X-Login-Ajax-call": 'true'
	            }
			}).then(function(response) {
                if (response.status == 201) {
                	console.log('Signup successfull-'+response.status);
					service.setUserProfile(response.data.id,username,email,socialSystem,socialId).then(function(setUProfRes){
						if(setUProfRes.status == 200){
							console.log('AuthenticationService.signin : Succesfully stored user profile in cookies');
						}else{
							console.log('AuthenticationService.signin : Failed to store user profile in cookies');
						}
					});
                	deferred.resolve(response);
					return deferred.promise;
                }
                else {
					 deferred.reject(response);
					 return deferred.promise;
                  
                }
            });
		};

		service.setUserProfile = function(id,username,email,socialSystem,socialDetail){
			var deferred = $q.defer();
			$rootScope.userProfile = {};
			$cookieStore.remove('userProfile');
			
			$rootScope.userProfile = {
					id: id,
					name: username,
					email: email,
					socialSystem: socialSystem,
					socialDetail : socialDetail
			};
			$cookieStore.put('userProfile', $rootScope.userProfile);
			var response = {"status": 200};
			deferred.resolve(response);
			 return deferred.promise;
		};
		
		service.getUserProfile = function(callback){
			console.log('Inside getUserProfile');
			var response = {};
			var userProfile = $cookieStore.get('userProfile') ;
			console.log('User Profile : '+userProfile);
			response.status = 200;
			response.data = userProfile;
			callback(response);
		};
		
		service.clearProfile = function(){
			$rootScope.userProfile = {};
			$cookieStore.remove('userProfile');
		};
		
        service.SetCredentials = function (username, password) {
            var authdata = Base64.encode(username + ':' + password);

            $rootScope.globals = {
                currentUser: {
                    username: username,
                    authdata: authdata
                }
            };

            $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; 
            $cookieStore.put('globals', $rootScope.globals);
        };

        service.ClearCredentials = function () {
            $rootScope.globals = {};
            $cookieStore.remove('globals');
            $http.defaults.headers.common.Authorization = 'Basic ';
        };

		
		
		
		service.Login = function (username, password, callback) {

        	console.log('Inside AuthenticationService--- username = '+username + ' , password = '+password );
        	
            /* Dummy authentication for testing, uses $timeout to simulate api call
             ----------------------------------------------*/

        	var postData = 'username=' + username + '&password=' + password ;
        	$http({
				method:'POST',
				url: '/GeoService/authenticate',
	            data: postData,
	            headers: {
	                    "Content-Type": "application/x-www-form-urlencoded",
	                    "X-Login-Ajax-call": 'true'
	            }
			}).then(function(response) {
                if (response.data == 'ok') {
                	console.log('Auth successfull-'+response.status);
                	 callback(response);
                }
                else {
                  alert("Invalid credentials");
                }
            });

        };
		
        return service;
    }])

.factory('Base64', function () {
    /* jshint ignore:start */

    var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';

    return {
        encode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output +
                    keyStr.charAt(enc1) +
                    keyStr.charAt(enc2) +
                    keyStr.charAt(enc3) +
                    keyStr.charAt(enc4);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);

            return output;
        },

        decode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                window.alert("There were invalid base64 characters in the input text.\n" +
                    "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                    "Expect errors in decoding.");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            do {
                enc1 = keyStr.indexOf(input.charAt(i++));
                enc2 = keyStr.indexOf(input.charAt(i++));
                enc3 = keyStr.indexOf(input.charAt(i++));
                enc4 = keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }

                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";

            } while (i < input.length);

            return output;
        }
    };

    /* jshint ignore:end */
});