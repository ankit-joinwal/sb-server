'use strict';

var app = angular.module('Dashboard');

app.controller('DashboardController',
				['$window','$scope', '$rootScope', '$routeParams','$location','AuthenticationService','DashboardService',
                function ($window,$scope, $rootScope, $routeParams,$location,AuthenticationService,DashboardService) {
	
	console.log("Inside DashboardController");
	
	$scope.editProfile = function(){
		$window.location.href = "/SociallBox/eo/home#/profile";
	};
	
	$scope.initCompanyPage = function(){
		AuthenticationService.isUserLoggedIn()
		.then(function(response){
			console.log('Inside DashboardController.isUserLoggedIn Response :'+response.status);
			AuthenticationService.getUserProfile()
			.then(function(profileResponse){
				var profile = profileResponse.data;
				$scope.userName = profile.name;
				$scope.emailId = profile.emailId;
				$scope.profilePic = profile.profilePic;
				$scope.userId = profile.userId;
				
				if(profile.status == 'COMPANY_NOT_LINKED'){
					$window.location.href = "/SociallBox/eo/home#/company/new";
				}else{
					$window.location.href = "/SociallBox/eo/home#/company";
				}
			});
			
		})
		.catch(function(response){
			console.log('Inside DashboardController.isUserLoggedIn Response :'+response.status);
			$window.location.href = "/SociallBox/eo/login";
		});
	};
	
	$scope.getProfileData = function(){
		AuthenticationService.isUserLoggedIn()
		.then(function(response){
			console.log('Inside DashboardController.isUserLoggedIn Response :'+response.status);
			AuthenticationService.getUserProfile()
			.then(function(profileResponse){
				var profile = profileResponse.data;
				$scope.userName = profile.name;
				$scope.emailId = profile.emailId;
				$scope.profilePic = profile.profilePic;
				$scope.userId = profile.userId;
			});
			
		})
		.catch(function(response){
			console.log('Inside DashboardController.isUserLoggedIn Response :'+response.status);
			$window.location.href = "/SociallBox/eo/login";
		});
	};
	
	
	
	$scope.initDashboardCards = function(){
		var userId ;
		AuthenticationService.getUserProfile()
		.then(function(profileResponse){
			var profile = profileResponse.data;
			userId = profile.userId;
			
			DashboardService.dashboardCards(userId)
			.then(function(dashboardResponse){
				$scope.dashboardCards = dashboardResponse.data.data;
			})
			.catch(function(dashboardResponse){
				alert('Unable to get details from server');
			});
		});
		
	};
	
	$scope.initDashboardCharts = function(){
		
		var userId ;
		AuthenticationService.getUserProfile()
		.then(function(profileResponse){
			var profile = profileResponse.data;
			userId = profile.userId;
			
			DashboardService.getMonthlyUsers(userId)
			.then(function(dashboardResponse){
				var labelArr = [];
				var dataArr = [];
				
				var monthlyUsers = dashboardResponse.data.data.attendees_by_month;
				$.each(monthlyUsers, function(idx, obj) {
					labelArr.push(obj.month);
					dataArr.push(obj.attendees);
					
				});
				
				var ctx, data, myBarChart, option_bars;
			   	  Chart.defaults.global.responsive = true;
			   	  ctx = $('#bar-chart').get(0).getContext('2d');
			   	  option_bars = {
			   	    scaleBeginAtZero: true,
			   	    scaleShowGridLines: true,
			   	    scaleGridLineColor: "rgba(0,0,0,.05)",
			   	    scaleGridLineWidth: 1,
			   	    scaleShowHorizontalLines: true,
			   	    scaleShowVerticalLines: false,
			   	    barShowStroke: true,
			   	    barStrokeWidth: 1,
			   	    barValueSpacing: 5,
			   	    barDatasetSpacing: 3,
			   	    legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].fillColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"
			   	  };
			   	  data = {
			   	    labels: labelArr,
			   	    datasets: [
			   	      {
			   	        label: "My First dataset",
			   	        fillColor: "rgba(26, 188, 156,0.6)",
			   	        strokeColor: "#1ABC9C",
			   	        pointColor: "#1ABC9C",
			   	        pointStrokeColor: "#fff",
			   	        pointHighlightFill: "#fff",
			   	        pointHighlightStroke: "#1ABC9C",
			   	        data: dataArr
			   	      }
			   	    ]
			   	  };
			   	  myBarChart = new Chart(ctx).Bar(data, option_bars);
			   	$("#monthlyUsers").removeClass('loader');
			})
			.catch(function(dashboardResponse){
				alert('Unable to get details from server');
			});
		});
	};
	
	$scope.getUserMessages = function(){
		var userId ;
		AuthenticationService.getUserProfile()
		.then(function(profileResponse){
			var profile = profileResponse.data;
			userId = profile.userId;
			
			DashboardService.getMessagesForUser(userId)
			.then(function(messagesResponse){
				$scope.userMessages = messagesResponse.data.data;
				$("#messages").removeClass('loader');
			})
			.catch(function(dashboardResponse){
				alert('Unable to get details from server');
			});
		})
		.catch(function(dashboardResponse){
			alert('Unable to get details from server');
		});
		
	};
	
}
]);