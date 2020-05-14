package com.ibm.db2.rca.service.analyze.dblevel;

import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.db2.rca.service.analyze.dblevel.InputToService;


public class PerformanceDegradationRootCauseAnalyzer 
{
	
	@Autowired
	private DbLevelAnalyzer dbLevelAnalyzer = null;
		
	/*
	 * 
	 */
	
	private InputToService inputToService = null;
	
	private OutputFromService outputFromService = null;
	

	
	public DbLevelAnalyzer getDbLevelAnalyzer() {
		return dbLevelAnalyzer;
	}

	public void setDbLevelAnalyzer(DbLevelAnalyzer dbLevelAnalyzer) {
		this.dbLevelAnalyzer = dbLevelAnalyzer;
	}

	public void setInputToService(InputToService inputToService)
	{
		this.inputToService = inputToService;
	}
	
	public PerformanceDegradationRootCauseAnalyzer()
	{		
		
	}
	
	
	public void firstStep() throws Exception
	{
		System.out.println(this.getClass().getSimpleName() + " : Object ID : " + this.hashCode());
		
		this.dbLevelAnalyzer.setInputToService(inputToService);
		this.dbLevelAnalyzer.analyze();	
		//this.setOutputFromService(this.dbLevelAnalyzer.getOutputFromService());
	}

	public OutputFromService getOutputFromService() {
		return outputFromService;
	}

	public void setOutputFromService(OutputFromService outputFromService) {
		this.outputFromService = outputFromService;
	}
}
