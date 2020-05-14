package com.ibm.db2.rca.spring.mvc.signup;

public class SignUpInputToService 
{
	private String organizationName = null;
	
	private String emailId = null;
	
	private String password = null;

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setInputToService(SignUpHttpJsonInputToService signUpHttpJsonInputToService)
	{
		this.emailId = signUpHttpJsonInputToService.getEmailId();
		
		this.password = signUpHttpJsonInputToService.getPassword();
		
		this.organizationName = signUpHttpJsonInputToService.getOrganizationName();				
	}
	
}
