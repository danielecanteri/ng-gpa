<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<spring:url value="/resources/" var="resourcesPath" />
<spring:url value="/webjars" var="webjarsPath" />

<!-- Bootstrap -->
<link href="${webjarsPath}/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">

<script src="${webjarsPath}/jquery/2.1.0/jquery.min.js"></script>

<script src="${webjarsPath}/jquery-ui/1.10.3/ui/jquery-ui.js"></script>

<script src="${webjarsPath}/angularjs/1.2.14/angular.min.js"></script>
<script src="${webjarsPath}/angularjs/1.2.14/angular-animate.min.js"></script>

<script src="${webjarsPath}/angularjs/1.2.14/angular-resource.min.js"></script>

<script src="${webjarsPath}/angularjs/1.2.14/angular-route.min.js"></script>

<%-- <script src="${webjarsPath}/angular-ui/0.4.0/angular-ui.min.js"></script> --%>

<script src="${webjarsPath}/fullcalendar/1.6.4/fullcalendar.min.js"></script>
<link href="${webjarsPath}/fullcalendar/1.6.4/fullcalendar.css" rel="stylesheet">

<link rel="stylesheet" href="${webjarsPath}/select2/3.4.5/select2.css" />
<script type="text/javascript" src="${webjarsPath}/select2/3.4.5/select2.js"></script>
<script type="text/javascript" src="${webjarsPath}/ui-select2/0.0.5/ui-select2.js"></script>

<link rel="stylesheet" href="${resourcesPath}/css/animate.css" />
<link rel="stylesheet" href="${resourcesPath}/css/app.css" />

<script src="${resourcesPath}/js/calendar.js"></script>
<script src="${resourcesPath}/js/app.js"></script>

<script type="text/javascript">
	XGPA = angular.extend(typeof XGPA === 'undefined' ? {} : XGPA, {
		contextPath : '<spring:url value="/" />'
	});
</script>
