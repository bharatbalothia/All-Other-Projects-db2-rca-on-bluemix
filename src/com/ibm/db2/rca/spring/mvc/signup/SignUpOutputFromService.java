package com.ibm.db2.rca.spring.mvc.signup;


public class SignUpOutputFromService  
{

	private String message = "";
	
	private boolean doesUserExist = false;
	
	private boolean userCreated = false;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isDoesUserExist() {
		return doesUserExist;
	}

	public void setDoesUserExist(boolean doesUserExist) {
		this.doesUserExist = doesUserExist;
	}

	public boolean isUserCreated() {
		return userCreated;
	}

	public void setUserCreated(boolean userCreated) {
		this.userCreated = userCreated;
	}


	
}
