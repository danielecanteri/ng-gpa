<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="revision navbar-text navbar-right">
		${revisionProperties.projectVersion} (rev.
		${revisionProperties.revision}, ${revisionProperties.committedDate})</div>
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-collapse">
			<span class="icon-bar"></span> <span class="icon-bar"></span> <span
				class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="<spring:url value="/" />">XGPA</a>
	</div>
	<div class="collapse navbar-collapse"></div>
	<!-- 		<div class="collapse navbar-collapse"> -->
	<!-- 			<ul class="nav navbar-nav"> -->
	<%-- 				<li><a id="nuovaInbound" href="<spring:url value="/pages/inbound/inbound-new"/>">Nuova Inbound</a></li> --%>
	<!-- 			</ul> -->
	<!-- 		</div> -->
</div>
