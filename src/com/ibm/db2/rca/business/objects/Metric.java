package com.ibm.db2.rca.business.objects;

import java.util.Comparator;
import java.util.Map;

import javax.annotation.Resource;

public class Metric 
{
	@Resource(name="metricBucketRelation_DbLevel")
	private Map<String,String> metricBucketRelation_DbLevel = null;
		
	@Resource(name="metricNameDBColumnNameRelation_DbLevel")
	private Map<String,String> metricNameDBColumnNameRelation_DbLevel = null;
	
	
	private String name = null;
	
	private double correlationValue = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCorrelationValue() {
		return correlationValue;
	}

	public void setCorrelationValue(double correlationValue) {
		this.correlationValue = correlationValue;
	}

	public class MetricComparator implements Comparator<Map.Entry<String, Metric>>
	{

		@Override
		public int compare(Map.Entry<String, Metric> o1, Map.Entry<String, Metric> o2)
		{

			int retVal = 0;

			if (o1.getValue().getCorrelationValue() > o2.getValue().getCorrelationValue()) 
			{
				retVal = 1;
			} 
			else if (o1.getValue().getCorrelationValue() < o2.getValue().getCorrelationValue())
			{
				retVal = -1;
			}				

			return retVal;
		}
		
	}


	

	
	

	
	
	
/*	public static Map<String,Metric> getMetricsFromCorrelationMap(Map<String, Double> metrics)
	{
		BeanWrapper wrapper = null;			

		Map<String, Object> properties = null;
				
		Iterator<String> itr = metrics.keySet().iterator();
		
		String key;
		
		Metric metric;
		
		Map<String,Metric> metricsMap = new HashMap<String,Metric>();
		
		while(itr.hasNext())
		{
			wrapper = new BeanWrapperImpl(Metric.class);
			
			properties = new HashMap<String, Object>();
			
			key = itr.next();
			
			properties.put("name", key);
			
			properties.put("correlationValue", metrics.get(key));
						
			wrapper.setPropertyValues(properties);
			
			
			
			metric = (Metric) wrapper.getWrappedInstance();
			
			//System.out.println("Metric : "+ metric.getName() + ", Correlation Value : " + metric.getCorrelationValue());
						
			metricsMap.put(key, metric);
			
		}
		
		return metricsMap;
	}*/

}
