package com.ibm.db2.rca.service.integration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ibm.db2.rca.JsonOutput;
import com.ibm.db2.rca.business.objects.Bucket;
import com.ibm.db2.rca.business.objects.Metric;



public class OutputFromService 
{
	
	private List<Map<String,Long>> dataFromDatabase = null;
	
	private LinkedHashMap<String, Metric> metricsDbLevel = null; 
	
	private LinkedHashMap<String, Bucket> sortedBuckets = null;
	
	private Map <Bucket, LinkedHashMap<String,Metric>> bucketMetricsMapRelation = null;
	
	private List<Object> traceData = null;
	
	//private com.ibm.db2.rca.spring.mvc.OutputFromService rawData = null;

	public List<Object> getTraceData() {
		return traceData;
	}

	public void setTraceData(List<Object> traceData) {
		this.traceData = traceData;
	}
	
//	public void setTraceData(com.ibm.db2.rca.spring.mvc.OutputFromService outputFromService) {
//		this.rawData = outputFromService;
//	}

	public List<Map<String, Long>> getDataFromDatabase() {
		return dataFromDatabase;
	}

	public void setDataFromDatabase(List<Map<String, Long>> dataFromDatabase) {
		this.dataFromDatabase = dataFromDatabase;
	}

	public LinkedHashMap<String, Metric> getMetricsDbLevel() {
		return metricsDbLevel;
	}

	public void setMetricsDbLevel(LinkedHashMap<String, Metric> metricsDbLevel) {
		this.metricsDbLevel = metricsDbLevel;
	}

	public LinkedHashMap<String, Bucket> getSortedBuckets() {
		return sortedBuckets;
	}

	public void setSortedBuckets(LinkedHashMap<String, Bucket> sortedBuckets) {
		this.sortedBuckets = sortedBuckets;
	}

	public Map<Bucket, LinkedHashMap<String, Metric>> getBucketMetricsMapRelation() {
		return bucketMetricsMapRelation;
	}

	public void setBucketMetricsMapRelation(
			Map<Bucket, LinkedHashMap<String, Metric>> bucketMetricsMapRelation) {
		this.bucketMetricsMapRelation = bucketMetricsMapRelation;
	}
	
	
	public JsonOutput getJsonOutput()
	{
		JsonOutput jsonOutput = new JsonOutput();
    	
    	JsonOutput.Chart chart = jsonOutput.new Chart();
    	
    	List<JsonOutput.Chart.Label> labels  = new ArrayList<JsonOutput.Chart.Label>();
    	
    	List<Double> values = new ArrayList<Double>();    	
    	
    	Metric metric = null;
    	
    	JsonOutput.Chart.Label label = null;
    	
    	Iterator <Metric> itr = this.metricsDbLevel.values().iterator();
    	
    	for(int i=1; itr.hasNext(); i++)
    	{
    		label = chart.new Label();
    		
    		metric = itr.next();
    		
    		label.setText(metric.getName());
    		
    		label.setValue(""+i);
    		
    		labels.add(label);
    		
        	values.add(metric.getCorrelationValue());
    	}
    	
    	chart.setyAxis(labels);
    	
    	chart.setxAxis(values);
    	
    	jsonOutput.setChart(chart);
    	
    	jsonOutput.setList(this.traceData);

		return jsonOutput;
		
	}
	
	

	
}
