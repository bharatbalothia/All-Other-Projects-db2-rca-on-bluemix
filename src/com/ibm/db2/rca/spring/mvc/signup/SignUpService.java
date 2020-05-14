package com.ibm.db2.rca.spring.mvc.signup;

import org.springframework.beans.factory.annotation.Autowired;

public class SignUpService 
{
	@Autowired
	private SignUpDao signUpDao = null;
	
	@Autowired
	private SignUpInputToService signUpInputToService = null;
	
	@Autowired
	private SignUpOutputFromService signUpOutputFromService = null;
	
	public void setInputToService(SignUpInputToService signUpInputToService)
	{
		this.signUpInputToService = signUpInputToService;
	}
	
	public void setInputToService(SignUpHttpJsonInputToService signUpHttpJsonInputToService)
	{
		this.signUpInputToService.setInputToService(signUpHttpJsonInputToService);
	}

	public void signUp() throws Exception 
	{
		if(this.signUpDao.queryUserExistence(this.signUpInputToService) == false)
		{
			this.signUpDao.insertUserRecord(this.signUpInputToService);
			
			this.signUpOutputFromService.setMessage("User created successfully.");
			
			this.signUpOutputFromService.setUserCreated(true);
		}
		else
		{
			this.signUpOutputFromService.setMessage("User already exists.");
			
			this.signUpOutputFromService.setUserCreated(false);
		}
	}

	public SignUpOutputFromService getOutputFromService() 
	{
		return this.signUpOutputFromService;
	}
	
	public void checkUserAvailability() throws Exception
	{
		boolean doesUserExist = false; 
		
		doesUserExist = this.signUpDao.queryUserExistence(this.signUpInputToService);
		
		if( doesUserExist == false)
		{
			this.signUpOutputFromService.setMessage("User does not exist.");		
		}
		else
		{
			this.signUpOutputFromService.setMessage("User already exists.");
		}
		
		this.signUpOutputFromService.setDoesUserExist(doesUserExist);
		
	}
}
