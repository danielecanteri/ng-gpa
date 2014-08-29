<div class="row" ng-controller="DebugController">
	<div class="col-md-2">
		<div>
			<button class="btn btn-info" ng-click="elencoCarichi()">Carichi</button>
		</div>
		<div>
			<button class="btn btn-info" ng-click="elencoJob()">Elenco
				job</button>
		</div>
		<div>
			<button class="btn btn-info" ng-click="elencoTask()">Elenco
				task</button>
		</div>
		<div>
			<button class="btn btn-info" ng-click="animate()">Animate</button>
		</div>

	</div>
	<div class="col-md-8">
		<pre>{{debug | json}}</pre>
		<div ng-show="showHide" class="showHide">this is a test</div>
		<div ng-repeat="item in items" class="showHide">{{item}}</div>
		<div>
			<h4>test select2</h4>
			<div>
				valore combo:
				<pre>{{form.value | json}}</pre>
			</div>
			<select ui-select2="uiConfig.selectOptions" ng-model="uiConfig.selectOptions._value"
			ng-change="uiConfig.selectOptions.ngChange()"
				style="display: block">
				<option value=""></option>
				<option data-ng-repeat="item in items" value="{{item.codice}}">{{item.codice}}
					- {{item.descrizione}}</option>
			</select>
		</div>
	</div>



	<div class="col-md-2"></div>
</div>