package com.ibm.db2.rca.spring.mvc.signup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SignUpController 
{
	@Autowired
	private SignUpService signUpService = null;

    @RequestMapping(value = "service/signup.json", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody SignUpOutputFromService signup(@RequestBody SignUpHttpJsonInputToService signUpHttpJsonInputToService) throws Exception 
    {    	
    	this.signUpService.setInputToService(signUpHttpJsonInputToService);
    	
    	this.signUpService.signUp();
    	
    	return this.signUpService.getOutputFromService();    	
    }
    
    @RequestMapping(value = "service/doesUserExist.json", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody SignUpOutputFromService doesUserExist(@RequestBody SignUpHttpJsonInputToService signUpHttpJsonInputToService) throws Exception 
    {    	    	
    	this.signUpService.setInputToService(signUpHttpJsonInputToService);
    	
    	this.signUpService.checkUserAvailability();
    	
    	return this.signUpService.getOutputFromService();        
    }
    
}


