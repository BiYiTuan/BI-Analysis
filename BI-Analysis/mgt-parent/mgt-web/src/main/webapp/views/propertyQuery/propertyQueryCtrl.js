define(['app', 'jquery'], function (app) {
    //console.dir(app);
	app.register.controller('PropertyQueryController', ['$location', '$modal', '$filter', '$scope', '$rootScope', '$state', function ($location, $modal, $filter, $scope, $rootScope, $state) {


		$scope.initializeController = function () {
			$rootScope.layout.hasSubs = false;
		}


	}]);
});
