'use strict';

angular.module('Home')

.factory('MeetupService',
	['$http',function($http){
		
		var service = {};
		
		
		
		
		service.createMeetup = function(title,description,organizerId,location,addressComponents,startDate,startTime,endDate,endTime,attendees,eventId,callback ){
			var startDateTime = startDate + ' '+ startTime;
			var endDateTime = endDate +  ' ' + endTime;
			
			var postData = '{ 	"title" : "' +title+ '",'+
								'"description" : "' + description+'", '+
								'"location" : ' +location+', '+
								'"startDate" : "' +startDateTime + '",'+
								'"endDate" : "' +endDateTime + '", '+
								'"organizerId" : "' +organizerId + '" , '+
								
								'"eventAtMeetup" : "' +eventId + '" , '+
								'"attendees" : ['+ attendees+
							']  ';
							
			if(addressComponents.length <=0){
				postData = postData + '}';
			}else{
				postData = postData + ' , "addressComponents" : ' +addressComponents + ' }';
			}
			console.log('Request ody for create meetup = '+JSON.stringify(postData));
			$http({
				method:'POST',
				url: '/GeoService/api/public/meetups',
	            data: postData,
	            headers: {
	                    "Content-Type": "application/json",
						"accept":"application/json",
	                    "X-Login-Ajax-call": 'true'
	            }
			}).then(function(response) {
                if (response.status == 201) {
                	console.log('Create meetup successfull-'+response.status);
                	 callback(response);
                }
                else {
                  alert("Create meetup failed");
                }
            });
			
			
		};
		
		service.addAttendees = function(meetupId,attendees,callback ){
			
			var postData = '{ 	"attendees" : ['+ attendees+
							']}';
			console.log('Request ody for create meetup = '+JSON.stringify(postData));
			$http({
				method:'POST',
				url: '/GeoService/api/public/meetups/'+meetupId+'/attendees',
	            data: postData,
	            headers: {
	                    "Content-Type": "application/json",
						"accept":"application/json",
	                    "X-Login-Ajax-call": 'true'
	            }
			}).then(function(response) {
                if (response.status == 200) {
                	console.log('Edit meetup successfull-'+response.status);
                	 callback(response);
                }
                else {
                  alert("Edit meetup failed");
                }
            });
			
			
		};
		
		service.getMeetupTimeSlots = function(callback){
			var response = {};
		var slots = ["12:00 AM","12:30 AM","01:00 AM","01:30 AM","02:00 AM","02:30 AM","03:00 AM","03:30 AM","04:00 AM","04:30 AM","05:00 AM","05:30 AM",
						"06:00 AM","06:30 AM","07:00 AM","07:30 AM","08:00 AM","08:30 AM","09:00 AM","09:30 AM","10:00 AM","10:30 AM","11:00 AM","11:30 AM",
						"12:00 PM","12:30 PM","01:00 PM","01:30 PM","02:00 PM","02:30 PM","03:00 PM","03:30 PM","04:00 PM","04:30 PM","05:00 PM","05:30 PM",
						"06:00 PM","06:30 PM","07:00 PM","07:30 PM","08:00 PM","08:30 PM","09:00 PM","09:30 PM","10:00 PM","10:30 PM","11:00 PM","11:30 PM"
						];
			response.status = 200;
			response.data = slots;
			callback(response);
		};
	
	
		service.getMeetup = function(meetupId,callback){
			console.log('inside MeetupService.getMeetup for '+meetupId);
			$http({
				method:'GET',
				url: '/GeoService/api/public/meetups/'+meetupId,
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   
			  callback(response);
			});
		};
		
		service.saveAttendeeResponse = function(meetupId,attendeeId,attendeeResponse,callback){
			console.log('inside MeetupService.saveAttendeeResponse for '+meetupId);
			var postData = '{ 	"attendeeResponse" : "'+ attendeeResponse+
							'"}';
			$http({
				method:'POST',
				url: '/GeoService/api/public/meetups/'+meetupId+'/attendees/'+attendeeId+'/response',
	            data: postData,
	            headers: {
	                    "Content-Type": "application/json",
						"accept":"application/json",
	                    "X-Login-Ajax-call": 'true'
	            }
			}).then(function(response) {
                if (response.status == 200) {
                	console.log('saveAttendeeResponse successfull-'+response.status);
                	 callback(response);
                }
                else {
                  alert("Edit meetup failed");
                }
            });
		};
		
		service.postMessageToMeetup = function(meetupId,socialId,message,callback){
			console.log('inside MeetupService.postMessageToMeetup for '+meetupId);
			var postData = '{ 	"message" : "'+ message+
							'"}';
			$http({
				method:'POST',
				url: '/GeoService/api/public/meetups/'+meetupId+'/attendees/'+socialId+'/message',
	            data: postData,
	            headers: {
	                    "Content-Type": "application/json",
						"accept":"application/json",
	                    "X-Login-Ajax-call": 'true'
	            }
			}).then(function(response) {
                if (response.status == 200) {
                	console.log('postMessageToMeetup successfull-'+response.status);
                	 callback(response);
                }
                else {
                  alert("postMessageToMeetup failed");
                }
            });
		};
		
		service.getMeetupMessages = function(meetupId,callback){
			console.log('inside MeetupService.getMeetupMessages for '+meetupId);
			$http({
				method:'GET',
				url: '/GeoService/api/public/meetups/'+meetupId+'/messages',
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   
			  callback(response);
			});
		};
		
		 return service;
	}
	]);