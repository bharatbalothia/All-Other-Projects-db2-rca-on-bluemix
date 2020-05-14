package com.ibm.db2.rca.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("customerId")
public class RestController 
{
	
	@RequestMapping(value= "/app/service/getDbNameListOfaCustomer.do", method = RequestMethod.POST, params={"methodName"})		
	public String conf( @RequestParam("methodName") String methodName)
	{
		if(methodName.compareToIgnoreCase("getDatabaseListForTheLoggedInCustomer") == 0)
		{
			
		}
					
		return "";
	}
}
