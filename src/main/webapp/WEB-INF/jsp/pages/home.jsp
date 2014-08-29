<div class="row" ng-controller="HomeController">
	<div class="col-md-2"></div>
	<div class="col-md-8">
		<button class="btn btn-info" ng-click="carica()">CARICA</button>
		<div ui-calendar="uiConfig.calendar" class="span8 calendar"
			ng-model="eventSources"></div>
	</div>


	<div class="col-md-2">
		<div ng-show="form.date != null" class="sidebox animated">
			<div class="row">
				<h2>{{form.date | date}}</h2>

				<table class="table">
					<tr ng-repeat="carico in carichiDelGiorno(form.date)">
						<td>{{carico.job}}</td>
						<td>{{carico.ore}}</td>
					</tr>
				</table>
				<pre> {{form | json}}</pre>
				<form role="form">
					<div class="form-group">
						<label for="job">Job</label> <select
							ui-select2="uiConfig.jobSelectOptions" ng-model="form.job"
							style="display: block">
							<option value=""></option>
							<option ng-repeat="job in jobs" value="{{job.codice}}"
								data-xuz="{{job.descrizione}}">{{job.codice}} -
								{{job.descrizione}}</option>
						</select>
						<span style="font-size: 10px">{{jobByCodice(form.job).descrizione}}</span>
					</div>
					<div class="form-group">
						<label for="task">Task</label> <select
							ui-select2="uiConfig.taskSelectOptions" ng-model="form.taskKey"
							style="display: block">
							<option value=""></option>
							<option ng-repeat="task in tasks" value="{{task.task + '#' + task.subtask + '#' + task.attivita}}">{{task.task}}
								- {{task.subtask}} - {{task.attivita}}</option>
						</select>
						<span style="font-size: 10px">{{taskByKey(form.taskKey).descrizione}}</span>
					</div>
					<div class="form-group">
						<label for="ore">Ore</label> <input type="text"
							class="form-control" id="ore" placeholder="Ore"
							ng-model="form.ore">
					</div>
					<button type="submit" class="btn btn-success"
						ng-click="formSalva()">SALVA</button>
					<button class="btn btn-link" ng-click="formAnnulla()">Annulla</button>
				</form>
			</div>
		</div>
	</div>
</div>