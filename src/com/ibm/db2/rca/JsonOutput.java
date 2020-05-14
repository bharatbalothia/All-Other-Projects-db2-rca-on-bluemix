package com.ibm.db2.rca;

import java.util.List;

public class JsonOutput 
{
	private Chart chart;
	
	private List<Object> list;
	
	
	public Chart getChart() {
		return chart;
	}


	public void setChart(Chart chart) {
		this.chart = chart;
	}
	
	public void setList(List<Object> list)
	{
		this.list = list;
	}
	
	public List<Object> getList()
	{
		return this.list;
	}


	public class Chart
	{
		private List<Label> yAxis;
		
		private List<Double> xAxis;
		

		public List<Label> getyAxis() {
			return yAxis;
		}


		public void setyAxis(List<Label> yAxis) {
			this.yAxis = yAxis;
		}

		public List<Double> getxAxis() {
			return xAxis;
		}


		public void setxAxis(List<Double> xAxis) {
			this.xAxis = xAxis;
		}

		public class Label 
		{
			private String value;
			
			private String text;
			
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getText() {
				return text;
			}
			public void setText(String text) {
				this.text = text;
			}
		}
	}

}
