package com.ibm.db2.rca.service.analyze.io;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.db2.rca.apache.stats.Correlation;
import com.ibm.db2.rca.application.Application;
import com.ibm.db2.rca.business.objects.MetricCollection;

public class IoAnalyzer 
{
	@Autowired
	private IoLevelAnalyzerDao ioLevelAnalyzerDao = null;
	
	@Autowired	
	private Application application;
	
/*	private MetricCollection rowsReadVsRowsReturnedMetricCollection;
	
	private MetricCollection ioMetricCollection;*/
	
	private Logger logger = Logger.getLogger(IoAnalyzer.class);
	
	private InputToService inputToService;
	
	private OutputFromService outputFromService;
	
	/**
	 * This property is set by Spring and is defined in Spring XML app-beans.xml file
	 */
	private double filterCriteriaCorrelationWithDbLevelValueMToEtoThisPropertyValue = 0.0D;
		


	public double getFilterCriteriaCorrelationWithDbLevelValueMToEtoThisPropertyValue() {
		return filterCriteriaCorrelationWithDbLevelValueMToEtoThisPropertyValue;
	}

	public void setFilterCriteriaCorrelationWithDbLevelValueMToEtoThisPropertyValue(
			double filterCriteriaCorrelationWithDbLevelValueMToEtoThisPropertyValue) {
		this.filterCriteriaCorrelationWithDbLevelValueMToEtoThisPropertyValue = filterCriteriaCorrelationWithDbLevelValueMToEtoThisPropertyValue;
	}

	public void setInputToService(InputToService inputToService)
	{
		this.inputToService = inputToService;
	}

	public InputToService getInputToService() 
	{
		return inputToService;
	}
	
	public void analyze() throws Exception
	{
		this.logger.debug("IoAnalyzer.analyze method called.");
	
		//double maxPercentageSumPoolReadTime = 0.0;
		
		//this.ioMetricCollection = this.inputToService.getIoMetricCollection();
		
		this.outputFromService= (OutputFromService) this.application.getBean(OutputFromService.class);
		
		MetricCollection initialMetricCollection = (MetricCollection) this.application.getBean(MetricCollection.class);
			
		List<Map<String, Object>> poolReadTimeDataSet = (List<Map<String, Object>>) this.ioLevelAnalyzerDao.queryAllBufferPoolReadTimeAlongWithDbPoolReadTime_LevelBufferPool();
		
		
		if(poolReadTimeDataSet == null || poolReadTimeDataSet.size() == 0)
		{
			this.outputFromService.setError(true);
			
			this.outputFromService.setErrorMessage("No data available for the given period.");
		}
		else
		{
			initialMetricCollection.setDataSet(poolReadTimeDataSet);
			
			List <String> skipMetricList = new ArrayList<String>();
			
			skipMetricList.add("TS");
			
			Map<String, Double> correlationValueMap = Correlation.getCorrelation(poolReadTimeDataSet, "DB_POOL_READ_TIME",skipMetricList);
			
			initialMetricCollection.updateMetricsCorrelationValue(correlationValueMap);
			
			initialMetricCollection.setMessage("Initial Buffer Pool Chart");
			
			this.outputFromService.setIoLevelRcaInitialMetricCollection(initialMetricCollection);
						
			MetricCollection correlationValueBasedFilteredMetricCollection = initialMetricCollection.getMetricCollectionWhereCorrelationValueMoreThanOrEqualToSpecified(this.filterCriteriaCorrelationWithDbLevelValueMToEtoThisPropertyValue);
			
			if(correlationValueBasedFilteredMetricCollection == null || correlationValueBasedFilteredMetricCollection.size() == 0)
			{
				
				String message = "None of the Buffer Pool Correlates to Performance Bottleneck.";
				
				this.logger.debug(message);
				
			}
			else
			{
				
				List bufferPoolReaTimeList = Arrays.asList(correlationValueBasedFilteredMetricCollection.getMetrics().keySet().toArray());
				
				List <String> bufferPoolNameList = new ArrayList<String>(bufferPoolReaTimeList.size());
				
				for(Object bufferPoolRealTime : bufferPoolReaTimeList)
				{
					String bufferPoolName = (String) bufferPoolRealTime;
					
					bufferPoolName = (String) bufferPoolName.subSequence(0, bufferPoolName.indexOf('_'));
					
					bufferPoolNameList.add(bufferPoolName);
				}
				
				Map<String, BigDecimal> maxPercentageContributionValues = (Map<String, BigDecimal>) this.ioLevelAnalyzerDao.queryMaxPercentageContributionOfGivenBufferPoolsToDbLevelPoolReadTime_LevelBufferPool(bufferPoolNameList);
				
				if(maxPercentageContributionValues == null || maxPercentageContributionValues.size() == 0)
				{
					MetricCollection maxPercentageContributionToTotalRequestFilteredMetricCollection = (MetricCollection) this.application.getBean(MetricCollection.class);
					
					maxPercentageContributionToTotalRequestFilteredMetricCollection = correlationValueBasedFilteredMetricCollection.getMetricCollectionWhereMaxPercentageContributionToTotalRequestTimeIsMoreThanOrEqualToValueSpecified_LevelDatabase(maxPercentageContributionValues);

					List listOfColumns = Arrays.asList(maxPercentageContributionToTotalRequestFilteredMetricCollection.getMetrics().keySet().toArray());
					
					listOfColumns.add("TS");
					listOfColumns.add("TOTAL_RQST_TIME");
					
					List<Map<String, Object>> list  = this.ioLevelAnalyzerDao.queryBufferPoolReadTimeForGivePoolNamesAlongWithDbPoolReadTime_LevelBufferPool(listOfColumns);
					
					maxPercentageContributionToTotalRequestFilteredMetricCollection.setDataSet(list);

					this.outputFromService.setIoLevelRcaFinalMetricCollection(maxPercentageContributionToTotalRequestFilteredMetricCollection);
								
				}
			}
		}
		
		
		/*this.rowsReadVsRowsReturnedMetricCollection = (MetricCollection) this.application.getBean(MetricCollection.class);

		List<Map<String, Object>> dataSet = this.ioLevelAnalyzerDao.getRowsReadVsRowsReturnRatioDataSet();
		
		this.rowsReadVsRowsReturnedMetricCollection.setDataSet(dataSet);
		
		ioMetricCollection.getMetrics().keySet().toArray();
		
		maxPercentageSumPoolReadTime = maxPercentageSumPoolReadTime = this.ioLevelAnalyzerDao.getMaxPercentageSumPoolReadTime();
		
		
		if (maxPercentageSumPoolReadTime < 30)
		{
			String message = "Although POOL_READ_TIME is strongly correlated but the time spent on I/O is within the acceptable range.";
			
			this.logger.debug(message);
		}
		else
		{
			if(this.ioLevelAnalyzerDao.getMaxReadResponseTime() > 10)
			{
				String message = "POOL_READ_TIME exceeds the acceptable limit of \"10 ms\" during bad performance";
				
				this.logger.debug(message);
				
			}
			
			
		}*/
		
	}

	public OutputFromService getOutputFromService() {
		return this.outputFromService;
	}
	
	/*private void analyzeBufferPoolReadTimeBottleneck() throws Exception
	{
		String message = null;
		
		double maxPercentageSumPoolReadTime = Metric.getMaxPercentageSumPoolReadTime(this.monitoredDBProperties, this.inputFromUser);
		
		if (maxPercentageSumPoolReadTime < 30)
		{
			message = "Although POOL_READ_TIME is strongly correlated but the time spent on I/O is within the acceptable range.";
			
			log.debug(message);
			
			this.appendToResult(message);
			
			this.logTrace.logTrace(message);
			
		}
		else
		{
			
			if(Metric.getMaxReadResponseTime(this.monitoredDBProperties, this.inputFromUser) > 10)
			{
				message = "POOL_READ_TIME exceeds the acceptable limit of \"10 ms\" during bad performance";
				
				log.debug(message);
				
				this.appendToResult(message);
				
				this.logTrace.logTrace(message);
			}
			
			CorrelationStrengthEnum correlationStrength = this.getRowsReadVsRowsReturnedRatioPerformanceCorrelationConfidenceLevel();
			
			if(correlationStrength == CorrelationStrengthEnum.STRONG || correlationStrength == CorrelationStrengthEnum.MEDIUM)
			{
				message = "Workload characteristics changes during bad performance and ROWS_READ/ROWS_SELECTED ratio also increases.";
				
				log.debug(message);
				
				this.appendToResult(message);
				
				this.logTrace.logTrace(message);
			}
			
			if(Metric.getRowsReadVsRowsReturnedRatio(monitoredDBProperties, inputFromUser) > 50)
			{
				message = "Rows read/Rows Selected) is higher side for OLTP. But it depends on characteristic of query.";
				
				log.debug(message);
				
				this.appendToResult(message);
				
				this.logTrace.logTrace(message);
			}
			
			this.logTrace.logTrace("Analyzing all Buffer Pools.");
			
			this.logTrace.logTrace("Initial Buffer Pools Matrices.");
			
			LinkedHashMap<String, Metric> matrices = this.getBufferPoolMatricesForReadTime();
			
			this.logTrace.logTraceMatrixCollection2(matrices);
			
			this.logTrace.logTrace("Filtering all Buffer Pools Matrices based on Medium Correlation. These are the Buffer Pool Contributing to IO - Buffer Pool Read Time Bottleneck.");
			
			LinkedHashMap<String, Metric> filteredMatrices = this.filterBufferPoolMatricesForReadTime(matrices, 0.9);
			
			this.logTrace.logTraceMatrixCollection2(filteredMatrices);
			
			System.out.println("Filtered Matrix Size : " +  filteredMatrices.size());
			
			String output = this.toString(filteredMatrices);
			
			System.out.println("Buffer Pool : \n" + output );
			
			this.appendToResult(output);
			

			
			//this.logTrace.logTrace("Analyzing all Buffer Pools for Hit Ratio.");
			
			//this.logTrace.logTrace("Initial Buffer Pools Hit Ratio Matrices.");
			
			//matrices = this.getBufferPoolHitRatioMatrices();
			
			//this.logTrace.logTraceMatrixCollection2(matrices);
			
			//this.logTrace.logTrace("Filtering all Buffer Pools Matrices based on Medium Correlation. These are the Buffer Pool Contributing to IO - Buffer Pool Hit Ratio Bottleneck.");
			
			//filteredMatrices = this.filterBufferPoolMatricesForReadTime(matrices, 0.9);

		}
	}*/

}
