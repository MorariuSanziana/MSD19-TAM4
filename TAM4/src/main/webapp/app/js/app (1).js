// WebClient URL is: 			http://localhost:8080/SCRUM/app/Start.html
// Rest Data Service URL is: 	http://localhost:8080/SCRUM/data/projects

var app = angular.module('App', ['ngGrid', 'ngRoute', 'ngResource']);
var studentsRestURL = 'http://localhost:8080/TAM4/resources/students';

//App navigation control
app.config(
		function($routeProvider){
			$routeProvider
				.when('/', 		{controller:'view1Controller', templateUrl: 'partials/view1.html'})
				.when('/view1', {controller:'view1Controller', templateUrl: 'partials/view1.html'})
				.when('/view2', {controller:'view2Controller', templateUrl: 'partials/view2.html'})
				.when('/view3', {controller:'view3Controller', templateUrl: 'partials/view3.html'})
				.otherwise({redirectTo: '/'});
		}
);