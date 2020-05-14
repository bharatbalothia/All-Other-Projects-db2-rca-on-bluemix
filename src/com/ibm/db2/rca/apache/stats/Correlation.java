package com.ibm.db2.rca.apache.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import com.ibm.db2.rca.dao.BaseDaoImpl;

public class Correlation 
{
	
	public static Map<String, Double> getCorrelation(List<Map< String, Object>> data, String targetColumnName, List<String> skipMetricList)	
	{		
		PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
		
		Map<String, Double> correlationValues = null;
		
		double [] dataArrayOfTargetColumn, dataArrayOfColumn; 
		
		dataArrayOfTargetColumn = BaseDaoImpl.getArray(data, targetColumnName);
		
		List<String> columnNames = BaseDaoImpl.getColumnNames(data);
		
		double corrVal = 0.0D; 
				
		String columnName;
				
		correlationValues = new HashMap<String, Double>();
		
		for(int i = 0 ; i < columnNames.size(); i++)
		{
			columnName = columnNames.get(i);
			
			if(targetColumnName.compareToIgnoreCase(columnName) == 0)
			{
				continue;
			}
			
			if(skipMetricList !=null && skipMetricList.contains(columnName))
			{
				continue;
			}
			
			dataArrayOfColumn = BaseDaoImpl.getArray(data, columnName);
			
			//printDoubleArray(dataArrayOfColumn);
			
			corrVal = 0.0D; 
			
			corrVal = pearsonsCorrelation.correlation(dataArrayOfColumn, dataArrayOfTargetColumn);
			
			if(Double.isNaN(corrVal)) 
			{
				corrVal = 0.0D;
				
			}
			
			//System.out.println("Column Name : " + columnName + ", Corr Value : " + corrVal);
			
			correlationValues.put(columnName, corrVal);
		}

		return correlationValues;
	}
	
	public static void printDoubleArray(double [] array)
	{
		System.out.println("------------");
		for(int i=0; i< array.length; i++)
		System.out.print(array[i] + ", ");
	}

}
