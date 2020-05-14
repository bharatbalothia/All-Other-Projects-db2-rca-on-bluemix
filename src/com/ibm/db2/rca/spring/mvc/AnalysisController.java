package com.ibm.db2.rca.spring.mvc;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.db2.rca.service.analyze.dblevel.DbLevelAnalyzer;
import com.ibm.db2.rca.service.analyze.dblevel.HttpJsonInputToService;
import com.ibm.db2.rca.service.analyze.dblevel.OutputFromService;
import com.ibm.db2.rca.service.analyze.dblevel.InputToService;

@Controller
public class AnalysisController
{
	@Autowired
	private DbLevelAnalyzer dbLevelAnalyzer = null;
	
	@Autowired
	private CustomerSession customerSession = null;
	
	@RequestMapping(value="/app/service/dbLevelAnalysis.do", method = RequestMethod.POST, produces={"application/json"})
    public @ResponseBody OutputFromService dbLevelAnalysisService(@RequestBody HttpJsonInputToService httpJsonInputToService, UsernamePasswordAuthenticationToken authToken) throws IOException, ParseException 
	{
		OutputFromService outputFromService = null;
		
		InputToService inputToService = new InputToService(httpJsonInputToService);
		
        if (authToken instanceof UsernamePasswordAuthenticationToken) 
        {   
        	String customerID = ((User) authToken.getPrincipal()).getUsername();
        	
        	inputToService.setSchemaName(customerID);        	
        }
        
        this.customerSession.setInputToService(inputToService);

		try 
		{
			
			this.dbLevelAnalyzer.setInputToService(inputToService);
			
			this.dbLevelAnalyzer.analyze();
			
			outputFromService = this.dbLevelAnalyzer.getOutputFromService();
			
		}
		catch (Exception e) 
		{		
			System.out.println("Exception.");
			e.printStackTrace();
		}

        return outputFromService;	
    }

}
