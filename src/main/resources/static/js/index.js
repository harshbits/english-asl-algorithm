var index = angular.module('indexPage', [ 'ngMaterial', 'ngMessages', 'md.data.table']);



index.run(function($templateCache, $http) {
    $http.get('source_files/retrieve-progress-bar.html').then(function(response) {
        $templateCache.put('retrieve-progress-bar', response.data);
    });
});

index.factory('inputService', function($http) {
	return {
		getEnglishParse : function(sentence) {
			return $http.get('entoasl/', {
				transformRequest : angular.identity,
				params : {
					sentence : sentence
				}
			});
		}
	}
});

index.controller('IndexController', ['$scope','$filter', '$mdDialog', 'inputService', '$templateCache',
                                     function($scope, $filter, $mdDialog, inputService, $templateCache, $timeout, $q){
	
	$scope.retrieve = function() {
		$scope.showRetrieveProgress(true, "", false);
		var promise = inputService.getEnglishParse($scope.sentence);
		promise
				.then(
						function(response) {
							console.log(response);
							$scope.wordTags  = response.data.engilshTagResponse.tagWords;
							$scope.correctedSentence = response.data.engilshTagResponse.grammarCorrected;
							$scope.aslSentence = response.data.sentence;
							$scope.showRetrieveProgress(
									false,
									"Retrieved all data.",
									false);
				});
	};
    
    $scope.showRetrieveProgress = function(value,
			retrieveMessage, isError) {
		if ((value == true)
				|| (value == false && isError == true)) {
			$mdDialog
					.show({
						clickOutsideToClose : false,
						preserveScope : true,
						template : $templateCache.get('retrieve-progress-bar'),
						locals : {
							value : value,
							retrieveMessage : retrieveMessage
						},
						controller : function DialogController(
								$scope, $mdDialog,
								value, retrieveMessage) {

							$scope.value = value;
							$scope.retrieveMessage = retrieveMessage;
							$scope.cancel = function() {
								$mdDialog.cancel();
							};
						}
					});
		} else {
			$mdDialog.cancel();
		}
	};
		
}]);
