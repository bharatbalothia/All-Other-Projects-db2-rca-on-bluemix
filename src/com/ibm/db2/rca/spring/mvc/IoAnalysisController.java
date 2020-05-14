package com.ibm.db2.rca.spring.mvc;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.db2.rca.service.analyze.io.IoAnalyzer;
import com.ibm.db2.rca.service.analyze.io.OutputFromService;

@Controller
public class IoAnalysisController
{
	@Autowired
	private IoAnalyzer dbLevelAnalyzer = null;
	
	@Autowired
	private CustomerSession customerSession = null;
	
	@RequestMapping(value="/app/service/ioLevelAnalysis.do", method = RequestMethod.POST, produces={"application/json"})
    public @ResponseBody OutputFromService ioLevelAnalysisService() throws IOException, ParseException 
	{
		OutputFromService outputFromService = null;
		
		//InputToService inputToService = new InputToService(httpJsonInputToService);

		try 
		{
			
			//this.dbLevelAnalyzer.setInputToService(inputToService);
			
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

	public IoAnalysisController() 
	{
		super();
	}
}
