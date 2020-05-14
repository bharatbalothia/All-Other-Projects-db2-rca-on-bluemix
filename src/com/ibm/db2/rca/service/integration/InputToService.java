package com.ibm.db2.rca.service.integration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;




import java.util.TimeZone;

public class InputToService 
{
	TimeZone timeZone = TimeZone.getTimeZone("Asia/Calcutta");
	
	private Date startDateTime = null;
	
	private Date endDateTime = null;
	
	private String dbName = null;

	public void validateUserInput() throws InputToServiceIllegalArgumentException
	{
		
		String errorMessage = "";
		
		if (this.startDateTime == null)
		{
			errorMessage = errorMessage + "Start Date Time input is missing.";
		}
		
		System.out.println("Input Start Time : " + this.startDateTime);
		
		if (this.endDateTime == null)
		{		
			errorMessage = errorMessage + "End Date Time input is missing.";
		}
		
		System.out.println("Input End Time : " + this.endDateTime);
		
		if (this.dbName == null)
		{
			errorMessage = errorMessage + "Monitored DB Configured DB Name input is missing.";
		}
		
		if(this.dbName.trim().length() == 0)
		{
			errorMessage = errorMessage + "Monitored DB Configured DB Name input is missing.";
		}
		
		if(errorMessage.trim().length() > 0)
		{
			throw (new InputToServiceIllegalArgumentException(errorMessage));
		}
		
	}

	public InputToService(HttpServletRequest request) 
	{

		this.startDateTime = new Date(Long.parseLong(request.getParameter("startTime")));
		
		this.endDateTime = new Date(Long.parseLong(request.getParameter("endTime")));
		
		this.dbName = request.getParameter("dbName");
		
		//System.out.println(this.startDateTime.toString() + this.endDateTime.toString() + this.monitoredDBConfiguredNameInPERFDB);

	}
	
	
	public InputToService(JsonInput jsonFormInput) throws ParseException 
	{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		
		this.startDateTime  = sdf.parse(jsonFormInput.getFromDateTime());
		
		this.endDateTime  = sdf.parse(jsonFormInput.getToDateTime());
		
		
		//System.out.println(this.startDateTime.toString() + this.endDateTime.toString() + this.monitoredDBConfiguredNameInPERFDB);

	}

	public InputToService(long startDateTime, long endDateTime, String dbName) 
	{

		this.startDateTime = new Date(startDateTime);
		
		this.endDateTime = new Date(endDateTime);
		
		this.dbName = dbName;
		
	}

	public Date getStartDateTime() 
	{
		return startDateTime;
	}

	public Date getEndDateTime() 
	{
		return endDateTime;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	
	
	
	


}

