"use strict";angular.module("webApp",["ngCookies","ngResource","ngSanitize","ngRoute"]).config(["$routeProvider",function(a){a.when("/",{templateUrl:"views/main.html",controller:"MainCtrl"}).otherwise({redirectTo:"/"})}]),angular.module("webApp").controller("MainCtrl",["$http","$scope",function(){}]);