<!DOCTYPE html>
<html ng-app="xgpa">
	<head>
		<title>Arca 4U</title>
		<jsp:include page="fragments/headTag.jsp" />
	</head>
	<body ng-controller="AppController">
		<jsp:include page="fragments/bodyHeader.jsp" />
		<div class="container" style="margin-top: 80px">
<!-- 			<div class="jumbotron"> -->
<!-- 				<h1>XGPA</h1> -->
<!-- 				<button ng-click="vai()">VAI</button> -->
<!-- 				<button ng-click="getCookie()">GET COOKIE</button> -->
<!-- 				<button ng-click="login()">LOGIN</button> -->
<!-- 				<div>Cookie: {{cookie}}</div> -->
<!-- 				<div>{{result}}</div> -->
<!-- 			</div> -->
			<div ng-view></div>
		</div>
		<jsp:include page="fragments/bodyFooter.jsp" />
	</body>
</html>