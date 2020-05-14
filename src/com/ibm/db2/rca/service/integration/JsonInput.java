package com.ibm.db2.rca.service.integration;

public class JsonInput 
{
	private String  fromDateTime = null;	
	private String  toDateTime = null;
	public String getFromDateTime() {
		return fromDateTime;
	}
	public void setFromDateTime(String fromDateTime) {
		this.fromDateTime = fromDateTime;
	}
	public String getToDateTime() {
		return toDateTime;
	}
	public void setToDateTime(String toDateTime) {
		this.toDateTime = toDateTime;
	}
}
