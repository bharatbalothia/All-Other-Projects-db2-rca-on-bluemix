package com.ibm.db2.rca.spring.mvc;

import java.util.Map;

import com.ibm.db2.rca.service.analyze.dblevel.InputToService;

public class CustomerSession 
{
	private String customerName = null;
	
	private Thread fileToDbUploadThread = null;
	
	private InputToService inputToService = null;
	
	private Map<String,String> customerDbList = null;

	public String getCustomerID() {
		return this.inputToService.getSchemaName();
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Map<String, String> getCustomerDbList() {
		return customerDbList;
	}

	public void setCustomerDbList(Map<String, String> customerDbList) {
		this.customerDbList = customerDbList;
	}

	public InputToService getInputToService() {
		return inputToService;
	}

	public void setInputToService(InputToService inputToService) {
		this.inputToService = inputToService;
	}

	public Thread getFileToDbUploadThread() {
		return fileToDbUploadThread;
	}

	public void setFileToDbUploadThread(Thread fileToDbUploadThread) {
		this.fileToDbUploadThread = fileToDbUploadThread;
	}
	
	
}
