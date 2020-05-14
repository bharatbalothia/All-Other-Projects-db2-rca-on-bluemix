package com.ibm.db2.rca.service.analyze.dblevel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputToService 
{
	private Date  startDateTime = null;	
	private Date  endDateTime = null;
	private String  dbName = null;
	private String  schemaName = null;
	
	public InputToService(HttpJsonInputToService httpJsonInputToService) throws ParseException
	{
		this.setDbName(httpJsonInputToService.getDbName());	
		
		this.setStartDateTime(httpJsonInputToService.getStartDateTime());
		
		this.setEndDateTime(httpJsonInputToService.getEndDateTime());		
	}
	
	public java.util.Date getStartDateTime()
	{				
		return this.startDateTime;
	}
	
	public void setStartDateTime(String startDateTime) throws ParseException {
		this.startDateTime = new SimpleDateFormat("yyyyMMddHHmm").parse(startDateTime);		
	}
	
	public java.util.Date getEndDateTime() throws ParseException 
	{			
		return this.endDateTime;
	}
	
	public void setEndDateTime(String endDateTime) throws ParseException {
		this.endDateTime = new SimpleDateFormat("yyyyMMddHHmm").parse(endDateTime);
	}
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}
}
