var xgpa = angular.module('xgpa',
		[ 'ngRoute', 'ngAnimate', 'ui.calendar', 'ui.select2' ]).factory(
		'cacheService', function() {
			return {
				put : function(key, object) {
					if (typeof (Storage !== 'undefined')) {
						sessionStorage[key] = JSON.stringify(object);
						return true;
					}
					return false;
				},
				get : function(key) {
					if (typeof (Storage !== 'undefined')) {
						var value = sessionStorage[key];
						if (angular.isUndefined(value)) {
							return undefined;
						}
						return JSON.parse(sessionStorage[key]);
					}
					return undefined;
				},
				remove : function(key) {
					if (typeof (Storage !== 'undefined')) {
						sessionStorage.removeItem(key);
					}					
					return true;
				}
			};
		}).factory(
		'remoteService',
		function($http, cacheService, $q) {
			return {
				elencoJob : function() {
					var tasks = cacheService.get("jobs");
					if (tasks == null) {
						var deferred = $q.defer();
						$http.get(XGPA.contextPath + 'app/elencoJob').success(
								function(response) {
									cacheService.put("jobs", response);
									deferred.resolve(response);
								});
						return deferred.promise;
					} else {
						return $q.when(tasks);
					}
				},
				elencoCarichi : function() {
					var deferred = $q.defer();
					$http.get(XGPA.contextPath + 'app/test').success(
							function(response) {
								deferred.resolve(response);
							});
					return deferred.promise;

				},
				elencoTask : function(job) {
					var tasks = cacheService.get("tasks-" + job);
					if (tasks == null) {
						var deferred = $q.defer();
						$http.get(XGPA.contextPath + 'app/elencoTask', {
							params : {
								job : job
							}
						}).success(function(response) {
							cacheService.put("tasks-" + job, response);
							deferred.resolve(response);
						});
						return deferred.promise;
					} else {
						return $q.when(tasks);
					}
				}
			};
		}).config(function($routeProvider) {
	$routeProvider.when('/login', {
		templateUrl : 'pages/login'
	}).when('/home', {
		templateUrl : 'pages/home'
	}).when('/debug', {
		templateUrl : 'pages/debug'
	}).otherwise({
		redirectTo : '/login'
	});
});

function AppController($scope, $http) {

	$scope.vai = function() {
		$http.get(XGPA.contextPath + 'app/home').success(function(response) {
			$scope.result = response;
		});
	}

	$scope.getCookie = function() {
		$http.get(XGPA.contextPath + 'app/getcookie').success(
				function(response) {
					$scope.cookie = response;
				});
	}
}

function LoginController($scope, $http, $location) {
	$scope.form = {};

	$scope.doLogin = function() {
		$http
				.get(XGPA.contextPath + 'app/login', {
					params : $scope.form
				})
				.success(
						function(response) {
							if (response == "\"OK\"") {
								$location.path('/home');
							} else {
								$scope.errors = [ "Errore durante login: nome utente o password errati" ];
							}
						});
	}

}

function HomeController($scope, $http, $location, remoteService) {
	$scope.form = {};

	$scope.carica = function() {
		remoteService.elencoCarichi().then(function(response) {
			$scope.carichi = response;
			for (var i = 0; i < response.length; i++) {
				console.log(response[i]);
				var parts = response[i].data.split('/');
				$scope.events.push({
					title : response[i].job,
					start : new Date(parts[2], parts[1] - 1, parts[0])
				});
			}
		});
	}

	$scope.carichiDelGiorno = function(date) {
		if (date == null || $scope.carichi == null) {
			return [];
		}
		var carichi = [];
		for (var i = 0; i < $scope.carichi.length; i++) {
			if ($scope.carichi[i].data == $scope.toData(date)) {
				console
						.log($scope.carichi[i].data + "=="
								+ $scope.toData(date))
				carichi.push($scope.carichi[i]);
			}
		}
		return carichi;
	}

	$scope.toData = function(date) {
		if (date == null)
			return null;
		return $scope.pad(date.getDate()) + "/"
				+ $scope.pad(date.getMonth() + 1) + "/" + date.getFullYear();
	}

	$scope.jobSelected = function() {
		$scope.tasks = [];
		if ($scope.form.job == null) {
			return;
		}
	}

	$scope.pad = function(str) {
		if (str == null)
			return null;
		if (("" + str).length == 1) {
			return "0" + str;
		}
		return str;
	}

	$scope.$watch('form.job', function() {
		console.log('job changed')
		console.log('job changed' + $scope.form)
		console.log('job changed' + $scope.form.job)

		if ($scope.form == null || $scope.form.job == null) {
			$scope.tasks = [];
			return;
		}
		remoteService.elencoTask($scope.form.job).then(function(response) {
			$scope.tasks = response;
		});

	});

	remoteService.elencoJob().then(function(response) {
		$scope.jobs = response;
	});

	$scope.dayClick = function(date, allDay, jsEvent, view) {
		console.log('day cliccked');
		console.log('date ' + date);
		console.log('allDay ' + allDay);
		console.log('jsEvent ' + jsEvent);
		console.log('view ' + view);

		$scope.form = {};
		$scope.form.date = $scope.toDateFormatted(date);
	}
	
	$scope.toDateFormatted = function(date) {
		var day = date.getDate() < 10 ? '0'+date.getDate():date.getDate();
		var month = (date.getMonth()+1) < 10 ? '0'+(date.getMonth()+1):(date.getMonth()+1);
		return day + '/' + month + '/' + date.getFullYear()
	}

	$scope.formAnnulla = function() {
		$scope.form = {};
	}

	$scope.formSalva = function() {
		$http.post(XGPA.contextPath + 'app/save', {
			params : {
				date : $scope.form.date,
				job : $scope.form.job,
				task : $scope.taskByKey($scope.form.taskKey).task,
				subtask : $scope.taskByKey($scope.form.taskKey).subtask,
				attivita : $scope.taskByKey($scope.form.taskKey).attivita,
				ore : $scope.form.ore
			}
		}).success(function(response) {
			console.log('salvato ' + response)
		});
		console.log('salva');

	}

	$scope.jobByCodice = function(codice) {
		for (var i = 0; i < $scope.jobs.length; i++) {
			if ($scope.jobs[i].codice == codice) {
				return $scope.jobs[i];
			}
		}
	}

	$scope.taskByKey = function(key) {
		for (var i = 0; i < $scope.tasks.length; i++) {
			if ($scope.tasks[i].task + '#' + $scope.tasks[i].subtask + '#' + $scope.tasks[i].attivita == key) {
				return $scope.tasks[i];
			}
		}
	}
	
	$scope.uiConfig = {
		calendar : {
			height : 450,
			editable : true,
			header : {
				left : 'title',
				center : '',
				right : 'today prev,next'
			},
			eventClick : $scope.alertOnEventClick,
			eventDrop : $scope.alertOnDrop,
			eventResize : $scope.alertOnResize,
			dayClick : $scope.dayClick
		},
		jobSelectOptions : {
			formatResult : function(object) {
				return object.id + '<br/><span style="font-size: 10px">'
						+ $scope.jobByCodice(object.id).descrizione + '</span>';
			},
			formatSelection : function(object) {
				return object.id;
			}
		},
		taskSelectOptions : {
			formatResult : function(object) {
				return $scope.taskByKey(object.id).subtask + ' ' + $scope.taskByKey(object.id).attivita + '<br/><span style="font-size: 10px">'
						+ $scope.taskByKey(object.id).descrizione + '</span>';
			},
			formatSelection : function(object) {
				if ($scope.taskByKey(object.id) != undefined)
					return $scope.taskByKey(object.id).subtask + ' ' + $scope.taskByKey(object.id).attivita;
			}
		}
	}

	/* event source that contains custom events on the scope */
	$scope.events = [

	];

	/* event sources array */
	$scope.eventSources = [ $scope.events ];

}

function DebugController($scope, $http, $location, remoteService, cacheService) {
	$scope.form = {};
	
	$scope.mostra = function(promise) {
		promise.then(function(response) {
			$scope.debug = response;
		});
	}

	$scope.elencoJob = function() {
		$scope.mostra(remoteService.elencoJob());
	}

	$scope.elencoCarichi = function() {
		$scope.mostra(remoteService.elencoCarichi());
	}

	$scope.elencoTask = function() {
		cacheService.remove("tasks-" + 'PARC14004P')
		$scope.mostra(remoteService.elencoTask('PARC14004P'));
	}

	$scope.items = [ 'aaaaa', 'bbbbb' ];

	$scope.animate = function() {
		$scope.showHide = !$scope.showHide;

		if ($scope.items.length == 2) {
			$scope.items.push('ccccc');
		} else {
			$scope.items.splice(2, 1);
		}
	}

	$scope.uiConfig = {
		selectOptions : {
			ngChange : function() {
				$scope.form.value = $scope.itemByCode($scope.uiConfig.selectOptions._value);
			}
		}
	}
	
	$scope.itemByCode = function(code) {
		for (var i = 0; i < $scope.items.length; i++) {
			if ($scope.items[i].codice == code) {
				return $scope.items[i];
			}
		}
	}

	$scope.items = [ {
		codice : "1",
		descrizione : "value1"
	}, {
		codice : "2",
		descrizione : "value2"
	}, {
		codice : "3",
		descrizione : "value3"
	}, {
		codice : "4",
		descrizione : "value4"
	} ]
}