<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
	<link type="text/css" rel="stylesheet" href="<c:url value="resources/js/lib/dijit/themes/claro/claro.css"/>" />
	
	<script>dojoConfig = {parseOnLoad: true}</script>
	<script src="<c:url value="resources/js/lib/dojo/dojo.js"/>"></script>
	<script>require(["dojo/parser", "dijit/form/Button"]);</script>
		
</head>

<body class="claro" onload='document.f.j_username.focus();'>

    
    
	<h3>DB2 Performance Degradation Root Cause Analyzer</h3>
	
	<form name="f" action="<c:url value='j_spring_security_check' />" method="POST">

		<table>
			<tr>
				<td>User:</td>
				<td>
					<input type="text" name="j_username" required="true" data-dojo-type="dijit/form/ValidationTextBox"/>
				</td>
			</tr>
			<tr>
				<td>Password:</td>
				<td>
					<input type="password" name="j_password" required="true" data-dojo-type="dijit/form/ValidationTextBox"/>
				</td>
			</tr>
			<tr>
				<td >
					<button data-dojo-type="dijit/form/Button" type="submit" name="loginButton" value="Log In">Log In</button>
				</td>

				<td>
					<button data-dojo-type="dijit/form/Button" type="reset">Reset</button>
				</td>
			</tr>
		</table>

	</form>
	
</body>

	
	<!-- ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}  -->

	
</html>