package com.ibm.db2.rca.business.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ibm.db2.rca.application.Application;
import com.ibm.db2.rca.business.objects.MetricCollection.DataSetName;

public class BucketCollection 
{	
	private String message = "";
	
	private String name = "";
	
	
	private Logger logger = Logger.getLogger(BucketCollection.class);
	
	private Map<Bucket, MetricCollection> bucketAndChildMetrics = null;
	
	private LinkedHashMap<String, Bucket> buckets = null;
	
	private List<Map<String, Object>> dataSet = null;
	
	@Resource(name="metricBucketRelation_DbLevel")
	private Map<String,String> metricBucketRelation_DbLevel = null;
	
	@Resource(name="bucketNameList")
	private List<String> bucketNameList = null;
	
	private @Value("${metricsVicinityRange}") double metricsVicinityRange;
	
	@Autowired	
	private Application application;
	
	
	
	public BucketCollection() 
	{
		this.buckets = new LinkedHashMap<String, Bucket>();
		
		this.bucketAndChildMetrics = new HashMap <Bucket, MetricCollection>(); 
	}

	public List<Map<String, Object>> getDataSet() {
		return dataSet;
	}

	public void setDataSet(List<Map<String, Object>> list) {
		this.dataSet = list;
	}
	
	

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

	public BucketCollection bucketize(MetricCollection metricCollection)
	{
		this.buckets.clear();
		
		this.bucketAndChildMetrics.clear();
		
		Iterator<String> bucketNameItr = this.bucketNameList.iterator();
		
		MetricCollection metricMetricCollection = null;
			
		String bucketName;
		
		List<String> listOfMetrics = null;
		
		Bucket bucket = null;
		
		while(bucketNameItr.hasNext())
		{
			bucketName = bucketNameItr.next();
			
			listOfMetrics = this.getMetricsNameList(this.metricBucketRelation_DbLevel, bucketName);
			
			Iterator<String> listOfMetricsitr = listOfMetrics.iterator();
			
			metricMetricCollection = (MetricCollection) this.application.getBean(MetricCollection.class);
			
			String metricName;
			Metric metric;
			
			while(listOfMetricsitr.hasNext())
			{
				metricName = listOfMetricsitr.next();
				
				metric = metricCollection.getMetric(metricName);
				
				if(metric!= null)
				{
					metricMetricCollection.put(metric);
				}
				
			}
			
			bucket = new Bucket();
			
			bucket.setName(bucketName);
			
			bucket.setCorrelationValue(metricMetricCollection.getCorrelationValue());
						
			this.buckets.put(bucketName, bucket);

			this.bucketAndChildMetrics.put(bucket, metricCollection);
			
		}
		
		this.reverseSort();
		
		return this;
	}
	
	
	
	
	public Bucket getBucket(String bucketName)
	{
		return this.buckets.get(bucketName);
	}
	
	public MetricCollection getMetricCollectionOfBucket(String bucketName)
	{
		MetricCollection metricCollection = null;
		
		if(bucketName!=null)
		{		
			Bucket bucket = this.buckets.get(bucketName);
		
			metricCollection = this.getMetricCollectionOfBucket(bucket);
			
		}
		
		return metricCollection;
	}
	
	
	public MetricCollection getMetricCollectionOfBucket(Bucket bucket)
	{
		MetricCollection metricCollection = null;
		
		if(bucket != null)
		{
			metricCollection = this.bucketAndChildMetrics.get(bucket);
		}
		
		return metricCollection;
	}
	
	public List<String> getMetricsNameList(Map<String,String> relation, String bucketName)
	{
		Iterator <String> itr = relation.keySet().iterator();
		
		List<String> listOfMetrics = new ArrayList<String>();
		
		String metricName;
		
		while(itr.hasNext())
		{
			metricName = itr.next();
			
			if(bucketName.compareTo(relation.get(metricName)) == 0 )
			{
				listOfMetrics.add(metricName);
			}			
		}
		
		return listOfMetrics;
	}
	
	private BucketCollection reverseSort()
	{
		
		List<Map.Entry<String, Bucket>> list = new LinkedList<Map.Entry<String, Bucket>>(this.buckets.entrySet());
		
		Bucket.BucketComparator comparator = new Bucket().new BucketComparator();
		
		Collections.sort(list, comparator);
	
		Collections.reverse(list);
		
		Iterator<Map.Entry<String, Bucket>> itr = list.iterator();
		
		BucketCollection bucketCollection = (BucketCollection) this.application.getBean(BucketCollection.class);
		
		Map.Entry<String, Bucket> entry = null;

		while (itr.hasNext()) 
		{
			entry = itr.next();

			bucketCollection.put(entry.getValue());
		}
 		
		return bucketCollection;
	}
	
	public void put(Bucket bucket)
	{
		this.buckets.put(bucket.getName(), bucket);
	}
}
