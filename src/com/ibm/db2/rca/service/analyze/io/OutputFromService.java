package com.ibm.db2.rca.service.analyze.io;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.db2.rca.application.Application;
import com.ibm.db2.rca.business.objects.MetricCollection;
import com.ibm.db2.rca.util.JsonCloner;

public class OutputFromService implements Serializable {

	@Autowired	
	private Application application;
	
	/**
	 * 
	 */
	
	private boolean error = false;
	
	private String errorMessage = "";
	
	private static final long serialVersionUID = 1L;

	private MetricCollection ioLevelRcaFinalMetricCollection;	
	
	private MetricCollection ioLevelRcaInitialMetricCollection;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
	

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public OutputFromService() 
	{
		this.listOfObjetMapForOutput = new ArrayList<Map<ObjectType, Object>>();
		
		this.listOfObjetMapForTrace = new ArrayList<Map<ObjectType, Object>>(); 
	}

	public enum ObjectType 
	{
		MetricCollection, BucketCollection, Metric, Bucket, String, Integer, Double, ListOfMapOfStringLong, MapOfStringDouble, MapOfStringMetric, ListOfMapOfStringMetric
	}

	private List<Map<ObjectType, Object>> listOfObjetMapForOutput = null;

	private List<Map<ObjectType, Object>> listOfObjetMapForTrace = null;

	public List<Map<ObjectType, Object>> getListOfObjetMapForOutput() {
		return listOfObjetMapForOutput;
	}

	public void setListOfObjetMapForOutput(
			List<Map<ObjectType, Object>> listOfObjetMapForOutput) {
		this.listOfObjetMapForOutput = listOfObjetMapForOutput;
	}

	public List<Map<ObjectType, Object>> getListOfObjetMapForTrace() {
		return listOfObjetMapForTrace;
	}

	public void setListOfObjetMapForTrace(List<Map<ObjectType, Object>> listOfObjetMapForTrace) {
		this.listOfObjetMapForTrace = listOfObjetMapForTrace;
	}

	public void addToTrace(ObjectType objectType, Object object) throws JsonGenerationException, JsonMappingException, IOException 
	{
		
		this.add(objectType, object, this.listOfObjetMapForTrace);
	}

	public void addToOutput(ObjectType objectType, Object object) throws JsonGenerationException, JsonMappingException, IOException 
	{	
		this.add(objectType, object, this.listOfObjetMapForOutput);
	}
	
	

	public MetricCollection getIoLevelRcaInitialMetricCollection() {
		return ioLevelRcaInitialMetricCollection;
	}

	public void setIoLevelRcaInitialMetricCollection(
			MetricCollection ioLevelRcaInitialMetricCollection) {
		this.ioLevelRcaInitialMetricCollection = ioLevelRcaInitialMetricCollection;
	}

	private void add(ObjectType objectType, Object object, List<Map<ObjectType, Object>> listOfObjetMap) throws JsonGenerationException, JsonMappingException, IOException 
	{

		if (objectType != null && object != null) 
		{			
			object = JsonCloner.clone(object);
			
			Map<ObjectType, Object> objectMap = new HashMap<ObjectType, Object>(1);

			objectMap.put(objectType, object);

			listOfObjetMap.add(objectMap);
		}
	}

	public void add(ObjectType objectType, Object object, boolean addToTraceAlso) throws JsonGenerationException, JsonMappingException, IOException 
	{
		this.addToOutput(objectType, object);

		if (addToTraceAlso) 
		{
			this.addToTrace(objectType, object);
		}

	}

	public MetricCollection getIoLevelRcaFinalMetricCollection() {
		return ioLevelRcaFinalMetricCollection;
	}

	public void setIoLevelRcaFinalMetricCollection(
			MetricCollection ioLevelRcaFinalMetricCollection) {
		this.ioLevelRcaFinalMetricCollection = ioLevelRcaFinalMetricCollection;
	}

}
