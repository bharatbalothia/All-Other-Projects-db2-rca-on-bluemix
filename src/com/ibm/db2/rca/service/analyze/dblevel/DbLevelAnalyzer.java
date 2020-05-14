package com.ibm.db2.rca.service.analyze.dblevel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ibm.db2.rca.apache.stats.Correlation;
import com.ibm.db2.rca.application.Application;
import com.ibm.db2.rca.business.objects.BucketCollection;
import com.ibm.db2.rca.business.objects.Metric;
import com.ibm.db2.rca.business.objects.MetricCollection;
import com.ibm.db2.rca.service.analyze.dblevel.OutputFromService.ObjectType;


public class DbLevelAnalyzer 
{
	
	private Logger logger = Logger.getLogger(DbLevelAnalyzer.class);
	
	@Autowired	
	private Application application;

	@Autowired
	private DBLevelAnalyzerDao dBLevelAnalyzerDao = null;
	
	
	
	//private BucketCollection bucketCollection = null;
	
	//private MetricCollection metricCollection = null;
	

	//Property Value Injection
	
	
	private @Value("${selectMetricsHavingCorrelationValueMoreThanOrEqualToThisValue}") double selectMetricsHavingCorrelationValueMoreThanOrEqualToThisValue;
	
	private @Value("${selectMetricsHavingMaxPercentageContributionToTotalRequestTimeMoreThanOrEqualToThisValue}") double selectMetricsHavingMaxPercentageContributionToTotalRequestTimeMoreThanOrEqualToThisValue;
	
	private @Value("${metricsVicinityRange}") double metricsVicinityRange;
	
	private @Value("${performanceMeasurementUnit}") String performanceMeasurementUnit;
	
	
	
	
	// Normal variable declaration
	private InputToService inputToService = null;
	
	private OutputFromService outputFromService = null;
	
	//private MetricCollection correlationValueBasedFilteredMetricCollection = null;
	
	//private MetricCollection maxPercentageContributionToTotalRequestFilteredMetricCollection = null;
	
	//private MetricCollection topCorrelationValueMetricsWithinTheSameVicinityMetricCollection = null;
	
	public InputToService getInputToService() {
		return inputToService;
	}

	public void setInputToService(
			InputToService inputToService) {
		this.inputToService = inputToService;
	}

	public String getPerformanceMeasurementUnit() {
		return performanceMeasurementUnit;
	}

	public void setPerformanceMeasurementUnit(String performanceMeasurementUnit) {
		this.performanceMeasurementUnit = performanceMeasurementUnit;
	}

	public void setOutputFromService(OutputFromService outputFromService) {
		this.outputFromService = outputFromService;
	}

	
	public DbLevelAnalyzer()
	{

	}
	
	public void analyze() throws Exception
	{
		
		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String rcaResultMessage = "";
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		logger.trace(methodName + " method called.");
		
		/**
		 * Actual Logic begins as follows:
		 */
		
		String message;
		
		this.outputFromService= (OutputFromService) this.application.getBean(OutputFromService.class);
		
		//this.metricCollection = (MetricCollection) this.application.getBean(MetricCollection.class);
		MetricCollection initialMetricCollection = (MetricCollection) this.application.getBean(MetricCollection.class);
		
		//this.bucketCollection = (BucketCollection) this.application.getBean(BucketCollection.class);		
		BucketCollection initialBucketCollection = (BucketCollection) this.application.getBean(BucketCollection.class);
		
		
		initialMetricCollection.setId("initialMetricCollection");
		
		List<Map<String, Object>> dataFromDatabase = this.dBLevelAnalyzerDao.queryMonitoredDataForAllComponents_LevelDatabase();
		
		if(dataFromDatabase == null || dataFromDatabase.size() == 0)
		{
			this.outputFromService.setError(true);
			
			message = "No data available for the given period.";
			logger.error(message);
			
			this.outputFromService.setErrorMessage(message);
			
			rcaResultMessage = message;
		}
		else
		{
		

		
			//this.metricCollection.addDataSet(DataSetName.TimeTaken, dataFromDatabase);
			
			//this.outputFromService.addToTrace(ObjectType.MetricCollection, this.metricCollection);
			
			//this.outputFromService.addToTrace(ObjectType.ListOfMapOfStringLong, dataFromDatabase);
					
			/**
			 * Get Correlation Value for All the Metrics
			 */
			
			List <String> skipMetricList = new ArrayList<String>();
			
			skipMetricList.add("TS");
						
			Map<String, Double> correlationValueMap = Correlation.getCorrelation(dataFromDatabase, this.performanceMeasurementUnit, skipMetricList);
			
			logger.debug("Correlation calculated on dataset.");
			
			//this.outputFromService.addToTrace(ObjectType.MapOfStringDouble, correlationValueMap);
			
			//this.printStringDoubleMap(correlationMap);
			
			//this.metricCollection.updateMetricsCorrelationValue(correlationValueMap);
			
			initialMetricCollection.updateMetricsCorrelationValue(correlationValueMap);
			
			initialMetricCollection.setDataSet(dataFromDatabase);
			
			logger.debug("Correlation updated in initial MetricCollection.");
			
			this.outputFromService.setDbLevelRcaInitialMetricCollection(initialMetricCollection);
			
			//this.outputFromService.addToTrace(ObjectType.MetricCollection, this.metricCollection);
			
			//Map<String, Metric> metricsMapUnsorted = this.getMetricsFromCorrelationMap(correlationMap);
			
			//this.outputFromService.addToTrace(ObjectType.MapOfStringMetric, metricsMapUnsorted);
			
			//this.printStringMetricMap(metricsMapUnsorted);
			
			//this.outputFromService.addToTrace(ObjectType.ListOfMapOfStringMetric, this.metricCollection);
			
			
			
			//this.bucketCollection.bucketize(this.metricCollection);
			
			/*
			 * Setting initial bucketCollection for Output -- Start
			 */
			
			initialBucketCollection.bucketize(initialMetricCollection);
			
			initialBucketCollection.setMessage("Metrics categorized in different Bottleneck Types before applying the various application level filtering logic.");
						
			List<String> listOfColumns = new ArrayList<String>(org.springframework.util.CollectionUtils.arrayToList(initialMetricCollection.getMetrics().keySet().toArray()));
			
			initialBucketCollection.setDataSet((List<Map<String,Object>>)this.dBLevelAnalyzerDao.queryMonitoredDataForGivenMetricsBucketize_LevelDatabase(listOfColumns));
			
			this.outputFromService.setDbLevelRcaIntialBucketCollection(initialBucketCollection);
			
			/*
			 * Setting initial bucketCollection for Output -- End
			 */
						
			//this.outputFromService.add(ObjectType.BucketCollection, this.bucketCollection, true);
			
			/**
			 * Correlation Value based filtering.
			 */
			MetricCollection correlationValueBasedFilteredMetricCollection = initialMetricCollection.getMetricCollectionWhereCorrelationValueMoreThanOrEqualToSpecified(this.selectMetricsHavingCorrelationValueMoreThanOrEqualToThisValue);
			
			if(correlationValueBasedFilteredMetricCollection == null || correlationValueBasedFilteredMetricCollection.size() == 0)
			{
				
				message = "DB2 RCA service didn't find any performance bottleneck during selected time period; "
						+ "or it may be possible that DB2 Database constantly performed on the same performance level during the given time period, "
						+ " without any variation. "
						+ "This application provides right RCA when it has performance statistics from good and bad period. Please select a different time period.";
						
				rcaResultMessage = message;
				
				this.logger.debug(message);
				
				
				//this.traceObjectList.add(message);
				
				//this.outputFromService.addToTrace(ObjectType.String, message);
							
				
			}
			else
			{
				//this.traceObjectList.add(this.correlationValueBasedFilteredMetricCollection);
				
				/*
				 * Setting final bucketCollection for Output -- Start
				 */
				
				BucketCollection finalResultBucketCollection = (BucketCollection) this.application.getBean(BucketCollection.class);
				
				finalResultBucketCollection.bucketize(correlationValueBasedFilteredMetricCollection);
				
				finalResultBucketCollection.setMessage("Metrics categorized in various Bottleneck Types after applying Performance Correlation filtering logic.");				
				
				listOfColumns = new ArrayList<String>(org.springframework.util.CollectionUtils.arrayToList(correlationValueBasedFilteredMetricCollection.getMetrics().keySet().toArray()));
				
				finalResultBucketCollection.setDataSet((List<Map<String,Object>>)this.dBLevelAnalyzerDao.queryMonitoredDataForGivenMetricsBucketize_LevelDatabase(listOfColumns));
				
				this.outputFromService.setDbLevelRcaFinalBucketCollection(finalResultBucketCollection);
				
				/*
				 * Setting final bucketCollection for Output -- End
				 */				
				
				message = "Applying Percentage Filtering where Time Consumed by Matrix is divide by TOTAL REQUEST TIME.";
				
				this.logger.debug(message);
				
				this.outputFromService.addToTrace(ObjectType.String, message);
				
				//this.traceObjectList.add(message);
				
				Map<String, BigDecimal> maxMetricsValues = this.dBLevelAnalyzerDao.queryMaxPercentageContributionOfAllComponentsToTotalRequestTime_LevelDatabase();
				
				MetricCollection maxPercentageContributionToTotalRequestFilteredMetricCollection = correlationValueBasedFilteredMetricCollection.getMetricCollectionWhereMaxPercentageContributionToTotalRequestTimeIsMoreThanOrEqualToValueSpecified_LevelDatabase(maxMetricsValues);
				
				if(maxPercentageContributionToTotalRequestFilteredMetricCollection == null || maxPercentageContributionToTotalRequestFilteredMetricCollection.size() == 0)
				{
					
					message = "None of the Matrix crossed the Max Percentage of Contribution to Total Request Time threshold limit of " + this.selectMetricsHavingMaxPercentageContributionToTotalRequestTimeMoreThanOrEqualToThisValue + "%. It does not seems to be performance bottleneck issue."; 
					
					//this.traceObjectList.add(message);
					
					this.logger.debug(message);	
					
					rcaResultMessage = "There is no performance bottleneck found during this given period of time.";
								
				}
				else
				{
					
					/*
					 * Setting final bucketCollection for Output -- Start
					 */
					
					finalResultBucketCollection = (BucketCollection) this.application.getBean(BucketCollection.class);
					
					finalResultBucketCollection.bucketize(maxPercentageContributionToTotalRequestFilteredMetricCollection);
					
					finalResultBucketCollection.setMessage("Metrics categorized in various Bottleneck Types after applying Metric Percentage Contribution filtering logic.");
										
					listOfColumns = new ArrayList<String>(org.springframework.util.CollectionUtils.arrayToList(maxPercentageContributionToTotalRequestFilteredMetricCollection.getMetrics().keySet().toArray()));
					
					finalResultBucketCollection.setDataSet((List<Map<String,Object>>)this.dBLevelAnalyzerDao.queryMonitoredDataForGivenMetricsBucketize_LevelDatabase(listOfColumns));
					
					this.outputFromService.setDbLevelRcaFinalBucketCollection(finalResultBucketCollection);
					
					/*
					 * Setting final bucketCollection for Output -- End
					 */
					
					
					message = "Following are the metrics having their Max Percentage of Contribution to Total Request Time more than " + this.selectMetricsHavingMaxPercentageContributionToTotalRequestTimeMoreThanOrEqualToThisValue + "%. These metric(s) may have contriuted o performance degradation.";
					
					this.logger.debug(message);
					
					//this.logger.debug(maxPercentageContributionToTotalRequestFilteredMetricCollection);
					
					
					
					//this.outputFromService.setDbLevelRcaBeforeVicinityMetricCollection(maxPercentageContributionToTotalRequestFilteredMetricCollection);
					

					
					//this.traceObjectList.add(message);
					
					//this.traceObjectList.add(this.maxPercentageContributionToTotalRequestFilteredMatrics);
					
					
					//listOfColumns = new ArrayList<String>(org.springframework.util.CollectionUtils.arrayToList(maxPercentageContributionToTotalRequestFilteredMetricCollection.getMetrics().keySet().toArray()));
					
					//listOfColumns.add("TS");
					//listOfColumns.add("TOTAL_RQST_TIME");
					
					//List<Map<String, Object>> list = this.dBLevelAnalyzerDao.queryMonitoredDataForAllComponents_LevelDatabase(listOfColumns);
					
					//maxPercentageContributionToTotalRequestFilteredMetricCollection.setDataSet(list);
					
					//this.outputFromService.setDbLevelRcaBeforeVicinityMetricCollection(maxPercentageContributionToTotalRequestFilteredMetricCollection);
					
					
					MetricCollection topCorrelationValueMetricsWithinTheSameVicinityMetricCollection = 
							maxPercentageContributionToTotalRequestFilteredMetricCollection.getMetricCorrelationTopRankedBasedOnCorrelationValueMetricsAreWithinTheSameVicinitySpecified(this.metricsVicinityRange);
					
					String  rcaFinalMetricCollectionMessage = "";
					
					if(topCorrelationValueMetricsWithinTheSameVicinityMetricCollection.size() == 1)
					{
						rcaFinalMetricCollectionMessage = "Following chart shows Performance v/s " + 
								(String) topCorrelationValueMetricsWithinTheSameVicinityMetricCollection.getMetrics().keySet().toArray()[0] ; 
														
						message = "Following is the single metric which is suspected to be the main contributor to the performance degradation based on the Vicnity Range " + topCorrelationValueMetricsWithinTheSameVicinityMetricCollection + ".";
						
						rcaResultMessage = (String) topCorrelationValueMetricsWithinTheSameVicinityMetricCollection.getMetrics().keySet().toArray()[0] + 
								" is the metric which is causing performance degradation.";
						
						
					}
					else
					{
						message = "Following are the metrics which are suspected to be the main contributor to the performance degradation based on the Vicnity Range " 
							+ this.metricsVicinityRange + ". But these are more than one metric withi same vicinity (close to each other) so its difficult to find which metric is the actual contributor to performance degradation. "
							+ "Now we are taking a different path and bucketizing these metrics for further analysis.";
						
						
						rcaResultMessage = "DB2 RCA found following metrics suspected to be the main contributor to the performance degradation. "
								+ "However It couldn't find the one single metric causing the performance degradation."
								+ "RCA recommends to user to further analyze the metrics and its characteristics by clicking on following links.";
						
						rcaFinalMetricCollectionMessage = "Following chart shows Performance v/s Metrics which are potentially causing performance degrdation."; 
								 
					}
					
					this.logger.debug(message);
					
					this.logger.debug(message);
					
					//this.logger.debug(topCorrelationValueMetricsWithinTheSameVicinityMetricCollection);
					
					//finalResultBucketCollection.setDataSet((List<Map<String,Object>>)this.dBLevelAnalyzerDao.queryMonitoredDataForAllComponentsBucketize_LevelDatabase());
	
					listOfColumns = new ArrayList<String>(org.springframework.util.CollectionUtils.arrayToList(topCorrelationValueMetricsWithinTheSameVicinityMetricCollection.getMetrics().keySet().toArray()));
					
					listOfColumns.add("TS");
					listOfColumns.add("TOTAL_RQST_TIME");
					
					dataFromDatabase = this.dBLevelAnalyzerDao.queryMonitoredDataForAllComponents_LevelDatabase(listOfColumns);
					
					topCorrelationValueMetricsWithinTheSameVicinityMetricCollection.setDataSet(dataFromDatabase);
					
					topCorrelationValueMetricsWithinTheSameVicinityMetricCollection.setMessage(rcaFinalMetricCollectionMessage);
					
					this.outputFromService.setDbLevelRcaFinalMetricCollection(topCorrelationValueMetricsWithinTheSameVicinityMetricCollection);
					
					
					
					
					//this.traceObjectList.add(message);
					
					//this.traceObjectList.add(this.selectedTopRankedBasedOnCorrelationValueMetricsAreWithinTheSameVicinitySpecified);
					
					//this.rawOutput.add(ObjectType.Metric, this.selectedTopRankedBasedOnCorrelationValueMetricsAreWithinTheSameVicinitySpecified);
					
				}
	
			}
		
		}
		
		this.outputFromService.setRcaResultMessage(rcaResultMessage);

	}
	
	
	/*public Map<String,Metric> getMetricsFromCorrelationMap(Map<String, Double> metrics)
	{
		BeanWrapper wrapper = null;

		Map<String, Object> properties = null;
				
		Iterator<String> itr = metrics.keySet().iterator();
		
		String key;
		
		Metric metric;
		
		Map<String,Metric> metricsMap = new HashMap<String,Metric>();
		
		while(itr.hasNext())
		{
			wrapper = new BeanWrapperImpl(this.application.metric());
			
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
	
	
	private void analyzeBottleneckBucket(Metric metric) throws Exception
	{		
/*		String bucketName = this.getMetricBucketName(metric);
		
		this.analyzeBottleneckPath(bucketName);			*/
	}
	
	
	private void analyzeBottleneckPath(String bucketName) throws Exception
	{
				
			if(bucketName.compareToIgnoreCase("IO") == 0)
			{
				
				
			}
			else if(bucketName.compareToIgnoreCase("CPU") == 0)
			{
				
			}
			else if(bucketName.compareToIgnoreCase("LOG") == 0)
			{
				
			}
			else if(bucketName.compareToIgnoreCase("LOCK") == 0)
			{
				
			}
			else if(bucketName.compareToIgnoreCase("NETWORK") == 0)
			{
				
			}
		
	}
	
	
	

		
	
	
/*	public List<String> getMetricsNameList(Map<String,String> relation, String bucketName)
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
	*/
	/*public void bucketize()
	{
		System.out.println(this.getClass().getSimpleName() + " : Object ID : " + this.hashCode());
		
		HashMap<String, Bucket> unsortedBuckets = new HashMap<String, Bucket>();
		
		LinkedHashMap<String, Metric> metrics;
		
		this.bucketMetricsMapRelation = new HashMap <Bucket, LinkedHashMap<String,Metric>> (); 
		
		Iterator<String> bucketNameItr = this.bucketNameList.iterator();
			
		String bucketName;
		
		List<String> listOfMetrics = null;
		
		Bucket bucket = null;
		
		while(bucketNameItr.hasNext())
		{
			bucketName = bucketNameItr.next();
			
			listOfMetrics = getMetricsNameList(this.metricBucketRelation_DbLevel, bucketName);
			
			Iterator<String> listOfMetricsitr = listOfMetrics.iterator();
			
			metrics = new LinkedHashMap<String, Metric>();
			
			String metricName;
			
			while(listOfMetricsitr.hasNext())
			{
				metricName = listOfMetricsitr.next();
				
				metrics.put(metricName, this.metricsDbLevel.get(metricName));
			}
			
			bucket = new Bucket();
			
			bucket.setName(bucketName);
			
			bucket.setCorrelationValue(this.getBucketCorrelation(metrics));
						
			unsortedBuckets.put(bucketName, bucket);

			this.bucketMetricsMapRelation.put(bucket, metrics);
			
		}
		
		this.sortedBuckets = Bucket.reverseSort(unsortedBuckets);
	}*/
	
	
	/*public double getBucketCorrelation(LinkedHashMap<String, Metric> metrics)
	{
		double bucketCorrelation = 0.0D;
		
		double sumOfMetricsCorrelationValue = 0.0D;
		
		int metricCount = 0;
		
		Iterator <Metric> itr = metrics.values().iterator();
		
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
			bucketCorrelation = sumOfMetricsCorrelationValue/metricCount;
		}
		
		return bucketCorrelation;
	}*/
	
	public void printStringMetricMap(Map<String, Metric> metrics)
	{
		Iterator<String> itr =  metrics.keySet().iterator();
		
		String key;
		
		Metric metric;
		
		while(itr.hasNext())
		{
			key = itr.next();
			
			metric = metrics.get(key);
			
			System.out.println("Key Name : " + key + ", Metric Name : " + metric.getName() + ",  Value: " + metric.getCorrelationValue());
			
		}
		
		
	}
	
	 
	
	public void printStringDoubleMap(Map<String, Double> correlationMap)
	{
		Iterator<String> itr =  correlationMap.keySet().iterator();
		
		String key;
		
		double correlationValue;
		
		while(itr.hasNext())
		{
			key = itr.next();
			
			correlationValue = correlationMap.get(key);
			
			System.out.println("Key Name : " + key + ", Correlation Value : " + correlationValue);
			
		}
	}
	
	
	/*public LinkedHashMap<String, Metric> selectMetricsWhereMaxPercentageContributionToTotalRequestTimeIsMoreThanOrEqualToValueSpecified_LevelDatabase(LinkedHashMap<String, Metric> metrics, double maxValueThreshhold) throws Exception 
	{
		 Variable Declaration 

		LinkedHashMap<String, Metric> filteredMetrics = null;

		Iterator<String> metricsKeySetItr = null;

		String metricName = null;
		
		String columnName = null;

		 Logic 
		
		
				
		Map<String, BigDecimal> maxMetricsValues = this.dBLevelAnalyzerDao.queryMaxPercentageContributionOfAllComponentsToTotalRequestTime_LevelDatabase(inputToService);

		metricsKeySetItr = metrics.keySet().iterator();

		filteredMetrics = new LinkedHashMap<String, Metric>();

		while (metricsKeySetItr.hasNext()) 
		{
			metricName = metricsKeySetItr.next();

			columnName = this.maxPercentageContributionOfAllComponentsToTotalRequestTimeColumnMappingWithMetrics.get(metricName);
			
			BigDecimal maxValue;
				
			System.out.println("ColumnName : " + columnName);
			
			maxValue = maxMetricsValues.get(columnName);
			
			System.out.println("maxValue : " + maxValue);
			
			if (maxValue.doubleValue() > maxValueThreshhold) 
			{				
				filteredMetrics.put(metricName, metrics.get(metricName));
			}			
		}
		
		return filteredMetrics;
	}*/

	public OutputFromService getOutputFromService() 
	{		
		return this.outputFromService;
	}
	
	

}
