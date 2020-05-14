package com.ibm.db2.rca.service.analyze.io;


public class InputToService 
{
	private String metricName = null;	
	
	private String bucketName = null;

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
/*	public InputToService(HttpJsonInputToService httpJsonInputToService)
	{
		this.bucketName = httpJsonInputToService.getBucketName();
		
		this.metricName = httpJsonInputToService.getMetricName();
	}*/

	
}
