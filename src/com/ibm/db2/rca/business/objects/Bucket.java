package com.ibm.db2.rca.business.objects;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Bucket
{
	
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

	public class BucketComparator implements Comparator<Map.Entry<String, Bucket>>
	{
		@Override
		public int compare(Map.Entry<String, Bucket> o1, Map.Entry<String, Bucket> o2)
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


	

}
