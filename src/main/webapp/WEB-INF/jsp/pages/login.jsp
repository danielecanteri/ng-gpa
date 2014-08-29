<div class="row">
	<div class="col-md-4"></div>
	<div class="col-md-4">
		<form role="form" ng-controller="LoginController">
			<div class="alert alert-danger" ng-show="errors != null">
				<div ng-repeat="error in errors">{{error}}</div>
			</div>
			<div class="form-group">
				<label for="exampleInputEmail1">Login</label> <input type="text"
					class="form-control" placeholder="login" ng-model="form.login">
			</div>
			<div class="form-group">
				<label for="exampleInputPassword1">Password</label> <input
					type="password" class="form-control" placeholder="password"
					ng-model="form.password">
			</div>
			<button type="submit" class="btn btn-default" ng-click="doLogin()">Submit</button>
		</form>
	</div>
	<div class="col-md-4"></div>
</div>