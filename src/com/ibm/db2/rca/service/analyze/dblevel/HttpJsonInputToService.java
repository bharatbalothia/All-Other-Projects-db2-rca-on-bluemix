package com.ibm.db2.rca.service.analyze.dblevel;


public class HttpJsonInputToService 
{
	private String  startDateTime = null;	
	private String  endDateTime = null;
	private String  dbName = null;

	
	public String getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	public String getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	
}
