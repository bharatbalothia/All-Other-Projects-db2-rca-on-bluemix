package com.ibm.db2.rca.spring.mvc.signup;

public class SignUpHttpJsonOutputFromService 
{
	private String message="";
	
	private boolean userExists=false;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isUserExists() {
		return userExists;
	}

	public void setUserExists(boolean userExists) {
		this.userExists = userExists;
	}
	
	

}
