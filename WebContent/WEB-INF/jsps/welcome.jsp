<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>

	<title>DB2 Performance Bottleneck Root Cause Analyzer | Welcome</title>
	
	<link rel="stylesheet" href="<c:url value="/resources/css/welcome.css"/>">
	
	<link rel="stylesheet" href="<c:url value="/resources/js/lib/dijit/themes/claro/claro.css"/>">

	<script>dojoConfig = {parseOnLoad: true}</script>
	
	<script src="<c:url value="/resources/js/lib/dojo/dojo.js"/>"></script>
	
	<script>
		require(["dojo/parser", "dijit/Dialog", "dijit/form/Button", "dijit/form/TextBox", "dojo/on", "dijit/form/ValidationTextBox", "dojox/form/PasswordValidator"]);
	</script>
	
	<script type="text/javascript">
	
		function actionLoginDialogShow( /*String*/ callee) 
		{
		
			var widget = {};
			 
	    	if(callee && callee === "SignUpDialog")
	    	{
	    		widget = dijit.byId("signUpFormDialog");

	    		widget.hide();
	    	}
	    	
	    	widget = dijit.byId("loginFormDialog");

	    	widget.show();

		}
		
		
		function actionSignUpDialogShow( /*String*/ callee) 
		{
			var widget = {};
			 
	    	if(callee && callee === "LogInDialog")
	    	{
	    		widget = dijit.byId("loginFormDialog");

	    		widget.hide();
	    	}

	    	widget = dijit.byId("signUpFormDialog");

	    	widget.show();

		}
		
		function actionLoginFormSubmit() 
		{
			document.f.submit();
		}
		
		function actionSignUpFormSubmit() 
		{
			var validForm = true;
			var emailId = dojo.byId("emailId").value.toLowerCase();
			var organizationName = dojo.byId("orgName").value;			
			var password = dojo.byId("password").value;			
			var confirmedPassword = dojo.byId("confirmedPassword").value;
			var formLevelErrorMessageDivDomNode = dojo.byId("formLevelErrorMessage");
			
			
			
			if(emailId === '')
			{
				emailIdMessage.innerHtml == 'Email Id is mandatory field.';
				
				validForm = false;								
			}
			if(organizationName === '')
			{
				orgNameMessage.innerHtml == 'Organization Name is mandatory field.';
				
				validForm = false;
			}
			if(password === '')
			{
				passwordMessage.innerHtml == 'Password is mandatory field.';
				
				validForm = false;
			}
			if(confirmedPassword === '')
			{
				confirmedPasswordMessage.innerHtml == 'Confirmed Password is mandatory field.';
				
				validForm = false;
			}			
			if(password !== confirmedPassword)
			{
				passwordMessage.innerHtml == 'Password and Confirmed Password must be same.';
				
				validForm = false;
			}
			
			if(validForm)
			{
				    
				var requestBodyToServer = {emailId : emailId, organizationName : organizationName, password: password};
				    
				var data = dojo.toJson(requestBodyToServer);
				    
				var checkUserAvailabilityService = {
						postData: data,										
						url: "<c:url value="service/signup.json"/>",
						handleAs: "json",
						method: "post",
						timeout: 30000,
						preventCache: true,
						headers: { "Content-Type": "application/json" },
						load: function(response){					
							if(response.userCreated)
							{
								formLevelErrorMessageDivDomNode.innerHTML = "User Created Successfully.";
								var statusDomNode = dojo.byId("status");								
								statusDomNode.value="Welcome! you have signed up successfully. You are now ready to Log-In.";
								document.signUp.submit();
							}
							else
							{
								formLevelErrorMessageDivDomNode.innerHTML = "Failed in user creation, either user already exists or there is some system error.";
							}
							
						},
						error: function(){
							console.debug("Some error has been occurred, please try sometime later.");
						}
				};
				
				dojo.xhrPost(checkUserAvailabilityService);
				
			}
			else
			{
				formLevelErrorMessageDivDomNode.innerHTML = "Fix the errors before hitting submit button.";
			}
		
						
				
			
			
		
	    	//document.getElementById("signupForm").submit();
		}
		
		function checkUsernameAvailability()
		{					
			var emailId = dojo.byId("emailId").value.toLowerCase();
			
			if(emailId !== '')
			{
						
				var userAvailabilityStatusNode = dojo.byId("userAvailabilityStatus");
				    
				var requestBodyToServer = {emailId : emailId};
				    
				var data = dojo.toJson(requestBodyToServer);
				    
				var checkUserAvailabilityService = {
						postData: data,										
						url: "<c:url value="service/doesUserExist.json"/>",
						handleAs: "json",
						method: "post",
						timeout: 30000,
						preventCache: true,
						headers: { "Content-Type": "application/json" },
						load: function(response){					
							if(response.doesUserExist)
							{
								userAvailabilityStatusNode.innerHTML = "This Email ID is already registered.";
							}
							else
							{
								userAvailabilityStatusNode.innerHTML = "This Email ID is available!";
							}
							
						},
						error: function(){
							console.debug("Some error has been occurred, please try sometime later.");
						}
				};
				
				dojo.xhrPost(checkUserAvailabilityService);
			
			}
			
			
		}
		
		
				
	</script>
		
</head>
<body class="claro">

	<div class="main">
		<div class="header">
			<div class="db2Logo"></div>
			<div class="headerTitle">Root Cause Analyzer (Performance Degradation) Service</div>
			<div class="filler3"></div>
			<div class="rightsideheaderbuttons">
				<div class="logindiv" onclick="actionSignUpDialogShow()"><span class="loginspan">SIGN UP</span></div>
				<div class="filler4"></div>		
				<div class="logindiv" onclick="actionLoginDialogShow()"><span class="loginspan">LOG IN</span></div>
			</div>
		</div>
		<div class="workspace">						
			<div class="filler1"></div>			
			<div class="status"><c:out value="${param.status}"/></div>			
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
	
	<div id="loginFormDialog" data-dojo-type="dijit/Dialog" data-dojo-id="loginFormDialog" title="Log In" >

	    <div class="dijitDialogPaneContentArea loginDialog" align="center">
	        
	        <form name="f" action="<c:url value='/j_spring_security_check' />" method="POST" align="center">
	        
   				<div class="signInLabel">
   					Log In
   				</div>
    				
				<hr class="hline"/>

				<div align="left" class="loginDialogLabel">
					Email Id															
				</div>
				
				<div class="loginTextBoxParentDiv">
					
					<input type="text" name="j_username" required="true" 
						data-dojo-type="dijit/form/TextBox"									
						data-dojo-props="placeHolder:'Email Id'"
						class="loginTextBox"
						required="true"
						style="text-align: center"/>
					
				</div>
				
				<div align="left" class="loginDialogLabel">
					Password															
				</div>
				
				<div class="loginTextBoxParentDiv">
					
					<input type="password" name="j_password" required="true" 
						data-dojo-type="dijit/form/TextBox"									
						data-dojo-props="placeHolder:'Password'"
						class="loginTextBox"
						required="true"
						style="text-align: center"/>
					
				</div>
				
				<hr class="hline"/>
				
				<div align="left" class="loginButtonSignupForgotPasswordParentDiv">
					<div align="left" class="loginDialogSubmitDiv" onclick="actionLoginFormSubmit()">
						<span class="loginspan">Log In</span>
					</div>
					
					<div align="right" class="otherLinks">
						<div class="signupLink">
							New User? <span class="signUpDialogSpanDiv" onclick="actionSignUpDialogShow('LogInDialog')">Sign Up</span> here											
						</div>
						
						<div class="signUpDialogSpanDiv">
							Forgot Password?											
						</div>
					</div>
				</div>

			</form>
			
	    </div>

	</div>
	
	
	<div id="signUpFormDialog" data-dojo-type="dijit/Dialog" data-dojo-id="signUpFormDialog" title="Sign Up" style="overflow:hidden;">

	    <div class="dijitDialogPaneContentArea signupDialog" align="center">
	        
	        <form name="signUp" action="<c:url value='/' />" method="POST" align="center">
	        
	        	<input type="hidden" name="status" id="status" value=""/>
	        
	        	<div align="left" id="formLevelErrorMessage" class="loginDialogLabel">					
				</div>
	        
   				<div class="signInLabel">
   					Sign Up
   				</div>
    				
				<hr class="hline"/>

				<div align="left" class="loginDialogLabel">
					Organization Name &nbsp;&nbsp;&nbsp;&nbsp;<span id="orgNameMessage" class="errorMessage"></span>
				</div>

				<div class="loginTextBoxParentDiv">
					
					<input type="text" name="orgName" id="orgName" required="true" 
						data-dojo-type="dijit/form/ValidationTextBox"									
						data-dojo-props="placeHolder:'Organization Name'"
						class="loginTextBox"
						required="true"
						style="text-align: center"/>					
				</div>


				<div align="left" class="loginDialogLabel">
					Email Id &nbsp;&nbsp;&nbsp;&nbsp;<span id="emailIdMessage" class="errorMessage"></span>
				</div>
				
				<div class="loginTextBoxParentDiv">
					
					<input type="text" name="emailId" id="emailId" required="true" 
						data-dojo-type="dijit/form/ValidationTextBox"									
						data-dojo-props="placeHolder:'Email Id',
						onBlur: checkUsernameAvailability"
						class="loginTextBox"
						required="true"
						style="text-align: center"/>
					
				</div>
				
				<div data-dojo-type="dojox/form/PasswordValidator" class="passwordValidator" name="password" id="password">
				
					<div align="left" class="loginDialogLabel">
						Password &nbsp;&nbsp;&nbsp;&nbsp;<span id="passwordMessage" class="errorMessage"></span>															
					</div>
					
					<div class="loginTextBoxParentDiv">
						
						<input type="password" name="password" pwType="new" required="true" 
							data-dojo-type="dijit/form/ValidationTextBox"									
							data-dojo-props="placeHolder:'Password'"
							class="loginTextBox"
							required="true" 
							style="text-align: center"/>
						
					</div>
					
					<div align="left" class="loginDialogLabel">
						Confirm Password &nbsp;&nbsp;&nbsp;&nbsp;<span id="confirmedPasswordMessage" class="errorMessage"></span>											
					</div>
					
					<div class="loginTextBoxParentDiv">
						
						<input type="password" name="confirmedPassword" required="true" 
							data-dojo-type="dijit/form/ValidationTextBox"									
							data-dojo-props="placeHolder:'Confirm Password'"
							class="loginTextBox"
							required="true"							
							style="text-align: center"
							pwType="verify"
							id="confirmedPassword"/>
						
					</div>
				
				</div>
				
				<hr class="hline"/>
				
				<div align="left" class="loginButtonSignupForgotPasswordParentDiv">
					<div align="left" class="loginDialogSubmitDiv" onclick="actionSignUpFormSubmit()">
						<span class="loginspan">Sign Up</span>
					</div>
					
					<div align="right" class="otherLinks">
						<div class="signupLink">
							Existing User? <span class="signUpDialogSpanDiv" onclick="actionLoginDialogShow('SignUpDialog')">Log In</span> here											
						</div>
						
						<div class="signUpDialogSpanDiv">
							Forgot Password?											
						</div>
					</div>
				</div>

			</form>
			
	    </div>

	</div>
	
	<c:out value="${j_spring_security_check}"/>
	
</body>
</html>