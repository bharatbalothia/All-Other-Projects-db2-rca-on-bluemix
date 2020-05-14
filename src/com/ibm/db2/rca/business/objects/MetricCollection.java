package com.ibm.db2.rca.business.objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ibm.db2.rca.application.Application;


public class MetricCollection 
{
	public enum DataSetName {TimeTaken};
	
	@Autowired	
	private Application application;
	
	@Resource(name="maxPercentageContributionOfAllComponentsToTotalRequestTimeColumnMappingWithMetrics")
	private Map<String, String> maxPercentageContributionOfAllComponentsToTotalRequestTimeColumnMappingWithMetrics = null;
	
	//Property Value Injection		
	private @Value("${selectMetricsHavingMaxPercentageContributionToTotalRequestTimeMoreThanOrEqualToThisValue}") double selectMetricsHavingMaxPercentageContributionToTotalRequestTimeMoreThanOrEqualToThisValue;
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String name;
	
	private String id;
	
	private LinkedHashMap<String, Metric> metrics = null;
	
	private Map<DataSetName, List> dataSets = null;

	private List<Map<String, Object>> dataSet = null;
	
	
	public void setDataSets(Map<DataSetName, List> dataSets) 
	{
		this.dataSets = dataSets;
	}

	public Metric getMetric(String metricName)
	{
		return this.metrics.get(metricName);
	}
	
	public Map<DataSetName, List> getDataSets()
	{
		return this.dataSets;
	}
	
	public List<Map<String, Object>> getDataSet() {
		return dataSet ;
	}

	public void setDataSet(List<Map<String, Object>> list) {
		this.dataSet = list;
	}
	
	public void put(Metric metric)
	{
		this.metrics.put(metric.getName(), metric);
	}

	public LinkedHashMap<String, Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(LinkedHashMap<String, Metric> metrics) {
		this.metrics = metrics;
	}
	
	public MetricCollection()
	{
		this.init();
	}
	
	public MetricCollection(MetricCollection metricCollection)
	{
		this.init();
		
		this.metrics.clear();
		this.metrics.putAll(metricCollection.getMetrics());		
	}
	
	public void setMetricCollection(MetricCollection metricCollection)
	{
		this.metrics.clear();
		this.metrics.putAll(metricCollection.getMetrics());		
	}
	
	private void init()
	{
		this.dataSets = new HashMap<DataSetName, List>(1);
		
		this.metrics = new LinkedHashMap<String, Metric>(); 
	}
	
	public void addDataSet(DataSetName dataSetName, List dataSet)
	{
		this.dataSets.put(dataSetName, dataSet);
	}
	
	@JsonIgnore
	public Metric getHighestCorrelationValueMetric()
	{
		Metric metric = null;
		
		this.reverseSort();
		
		if(this.metrics != null && this.metrics.size() > 0)
		{
			metric = this.metrics.entrySet().iterator().next().getValue();
		}
		
		return metric;
	}
	
	



	@JsonIgnore
	public double getCorrelationValue()
	{
		double correlationValue = 0.0D;
		
		double sumOfMetricsCorrelationValue = 0.0D;
		
		int metricCount = 0;
		
		Iterator <Metric> itr = this.metrics.values().iterator();
		
		Metric metric;
		
		while(itr.hasNext())
		{			
			
			metric = itr.next();
			
			//System.out.println("Metric : "+metric.getName() + ", Value : " + metric.getCorrelationValue());
			
			if (metric.getCorrelationValue() > 0)
			{
				metricCount++;
				
				sumOfMetricsCorrelationValue = sumOfMetricsCorrelationValue + metric.getCorrelationValue();					
			}			
		}
		
		if(metricCount > 0 && sumOfMetricsCorrelationValue > 0.0D)
		{
			correlationValue = sumOfMetricsCorrelationValue/metricCount;
		}
		
		return correlationValue;
	}
	
	public MetricCollection updateMetricsCorrelationValue(Map<String, Double> metricsCorrelationValue)
	{
		BeanWrapper wrapper = null;

		Map<String, Object> properties = null;
				
		Iterator<String> itr = metricsCorrelationValue.keySet().iterator();
		
		String metricName;
		
		Metric metric;
		
		double correlationValue = 0.0D; 
		
		while(itr.hasNext())
		{
				
			metricName = itr.next();
			
			metric = this.getMetric(metricName);
			
			correlationValue = metricsCorrelationValue.get(metricName);
			
			if(metric == null)
			{				
				wrapper = new BeanWrapperImpl(this.application.getBean(Metric.class));
				
				properties = new HashMap<String, Object>();
				
				properties.put("name", metricName);
				
				properties.put("correlationValue", correlationValue);
							
				wrapper.setPropertyValues(properties);
				
				metric = (Metric) wrapper.getWrappedInstance();
			}
			else
			{
				metric.setCorrelationValue(correlationValue);
			}			
			
			//System.out.println("Metric : "+ metric.getName() + ", Correlation Value : " + metric.getCorrelationValue());
						
			this.put(metric);
			
		}
		
		this.reverseSort();
		
		return this;
	}
	
	
	/*private static LinkedHashMap<String,Metric> reverseSort(Map<String,Metric> metrics)
	{
		List<Map.Entry<String, Metric>> list = new LinkedList<Map.Entry<String, Metric>>(metrics.entrySet());
		
		Metric.MetricComparator comparator = new Metric().new MetricComparator();
		
		Collections.sort(list, comparator);
	
		Collections.reverse(list);
		
		Iterator<Map.Entry<String, Metric>> itr = list.iterator();
		
		LinkedHashMap<String, Metric> sortedMap = new LinkedHashMap<String, Metric>();

		Map.Entry<String, Metric> entry = null;

		while (itr.hasNext()) 
		{
			entry = itr.next();

			sortedMap.put(entry.getKey(),entry.getValue());
		}
 		
		return sortedMap;
	}*/
	
	
	private MetricCollection reverseSort()
	{
		List<Map.Entry<String, Metric>> list = new LinkedList<Map.Entry<String, Metric>>(this.metrics.entrySet());
		
		Metric.MetricComparator comparator = new Metric().new MetricComparator();
		
		Collections.sort(list, comparator);
	
		Collections.reverse(list);
		
		Iterator<Map.Entry<String, Metric>> itr = list.iterator();
		
		LinkedHashMap<String, Metric> sortedMap = new LinkedHashMap<String, Metric>();

		Map.Entry<String, Metric> entry = null;

		while (itr.hasNext()) 
		{
			entry = itr.next();

			sortedMap.put(entry.getKey(),entry.getValue());
		}
		
		this.setMetrics(sortedMap);
 		
		return this;
	}
	
	public int size()
	{
		return this.metrics.size();
	}
	
	
	public MetricCollection getMetricCollectionWhereCorrelationValueMoreThanOrEqualToSpecified(double correlationValue)
	{
		MetricCollection metricCollection = (MetricCollection) this.application.getBean(MetricCollection.class);
		
		Iterator<Map.Entry<String,Metric>> itr = this.metrics.entrySet().iterator();
		
		Entry <String,Metric> entry = null;
		
		while(itr.hasNext())
		{
			entry = itr.next();
			
			if(entry.getValue().getCorrelationValue() >= correlationValue)
			{
				metricCollection.put(entry.getValue());
			}			
		}
		
		//metricCollection = this.copyRelevantDataSetColumns(metricCollection);
		
		return metricCollection;		
	}
	
	public MetricCollection getMetricCollectionWhereMaxPercentageContributionToTotalRequestTimeIsMoreThanOrEqualToValueSpecified_LevelDatabase(Map<String, BigDecimal> maxMetricsValues) throws Exception 
	{
		/* Variable Declaration */
		
		MetricCollection metricCollection = null;		

		Iterator<String> metricsKeySetItr = null;

		String metricName = null;
		
		String columnName = null;
		
		BigDecimal maxValue = null;

		/* Logic */
		

		metricsKeySetItr = this.metrics.keySet().iterator();
		
		metricCollection = (MetricCollection) this.application.getBean(MetricCollection.class);
		

		while (metricsKeySetItr.hasNext()) 
		{
			metricName = metricsKeySetItr.next();

			columnName = this.maxPercentageContributionOfAllComponentsToTotalRequestTimeColumnMappingWithMetrics.get(metricName);

			//System.out.println("ColumnName : " + columnName);
			
			maxValue = maxMetricsValues.get(columnName);
			
			//System.out.println("maxValue : " + maxValue);
			
			if (maxValue.doubleValue() > this.selectMetricsHavingMaxPercentageContributionToTotalRequestTimeMoreThanOrEqualToThisValue) 
			{				
				metricCollection.put(this.metrics.get(metricName));
			}			
		}
		
		//metricCollection = this.copyRelevantDataSetColumns(metricCollection);
		
		return metricCollection;
	}
	
	
	public MetricCollection getMetricCorrelationTopRankedBasedOnCorrelationValueMetricsAreWithinTheSameVicinitySpecified(double vicinity)
	{
		MetricCollection metricCollection = null;
		
		Entry <String, Metric> preEntry = null, currEntry = null;
		
		Iterator<Map.Entry<String, Metric>> itr = this.metrics.entrySet().iterator();
		
		metricCollection = (MetricCollection) this.application.getBean(MetricCollection.class);
		
		if(this.metrics.size() == 1)
		{
			
			metricCollection.setMetricCollection(this);
			
			return metricCollection;
		}
		
		if(itr.hasNext())
		{
			currEntry = (Entry <String, Metric>) itr.next();
		}
		
		while(itr.hasNext())
		{
			
			metricCollection.put(currEntry.getValue());
			
			preEntry = currEntry;
			
			currEntry = (Entry <String, Metric>) itr.next();
			
			if(preEntry.getValue().getCorrelationValue() - vicinity > currEntry.getValue().getCorrelationValue())
			{			
				break;
			}			
		}
		
		if(preEntry != null && currEntry != null)
		{
			if(preEntry.getValue().getCorrelationValue() - vicinity <= currEntry.getValue().getCorrelationValue())
			{			
				metricCollection.put(currEntry.getValue());
			}
		}
		
		//metricCollection = this.copyRelevantDataSetColumns(metricCollection);

		return metricCollection;	
	}
	
	private MetricCollection copyRelevantDataSetColumns(MetricCollection metricCollection)
	{		
		if(metricCollection != null)
		{
			metricCollection.dataSets.clear();
			
			if(metricCollection.metrics != null)
			{		
				if(this.dataSets!=null)
				{
					Iterator<DataSetName> dataSetNameKeyItr = this.dataSets.keySet().iterator();
					
					DataSetName dataSetName = null;
					
					List dataSet = null;
					
					while (dataSetNameKeyItr.hasNext())
					{
						dataSetName = dataSetNameKeyItr.next();
						
						dataSet = this.dataSets.get(dataSetName);
						
						if(dataSet != null)
						{						
							Iterator dataSetRowItr = dataSet.iterator();
							
							List<HashMap<String,Object>> targetDataSet = new ArrayList<HashMap<String,Object>>();
							
							while(dataSetRowItr.hasNext())
							{								
								Map<String, Object> dataSetRow = (Map<String, Object>) dataSetRowItr.next();
								
								Map<String, Object> clonneddataSetRow = new HashMap<String, Object>();
														
								Iterator<String> metricsItr = metricCollection.metrics.keySet().iterator();
								
								while(metricsItr.hasNext())
								{
									String metricName = metricsItr.next();
									
									Object object = dataSetRow.get(metricName);	
									
									if( object != null)
									{
										clonneddataSetRow.put(metricName, object);									
									}
								}
								
								targetDataSet.add((HashMap<String, Object>) clonneddataSetRow);
							}
							
							metricCollection.addDataSet(dataSetName, targetDataSet);
						}					
					}
				}
			}
		}
		
		return metricCollection;
		
	}
	
	

}
