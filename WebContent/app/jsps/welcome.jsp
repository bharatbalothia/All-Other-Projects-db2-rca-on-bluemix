<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" href="./resources/css/welcome.css">
	<title>Welcome to DB2 Performance Degradation Root Cause Analyzer</title>
	
	<script type="text/javascript">
	
		function actionLoginFormSubmit() 
		{
	    	document.getElementById("loginForm").submit();
		}
		
		function actionSignupFormSubmit() 
		{
	    	document.getElementById("signupForm").submit();
		}
		
	</script>
</head>
<body>	
	<div class="main">
		<div class="header">
			<div class="db2Logo"></div>
			<div class="headerTitle">Root Cause Analyzer (Performance Degradation) Service</div>
			<div class="filler3"></div>
			<div class="rightsideheaderbuttons">
				<div class="logindiv" onclick="actionLoginFormSubmit()"><span class="loginspan">SIGN UP</span></div>
				<div class="filler4"></div>		
				<div class="logindiv" onclick="actionSignupFormSubmit()"><span class="loginspan">LOG IN</span></div>
			</div>
		</div>
		<div class="workspace">						
			<div class="filler1"></div>
			<div class="title">Root Cause Analyzer Service</div>
			<div class="filler2"></div>  	
			<div class="subTitle">Single click solution to identify DB2 Performance Bottleneck</div>
		</div>
		<div style="display: none;">
				<form id="loginForm" name="loginForm" method="post" action="login">										
				</form>
				<form id="signupForm" name="signupForm" method="post" action="signup">										
				</form>
		</div>
		<div class="footer">
		</div>
	</div>	  
</body>
</html>