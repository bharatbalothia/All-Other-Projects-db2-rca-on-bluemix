package com.ibm.db2.rca.service.analyze.component.bufferpool;

public class BufferPool
{
	private double minBufferPoolHitRatio = -1.0D;
	
	private double avgBufferPoolHitRatio = -1.0D;
	
	private int bufferPoolId = -1;
	
	private String bufferPoolName = null;
	
	private int missedThresholdCount = -1;
	
	private int rowCount = -1;
	
	

	public double getMinBufferPoolHitRatio() {
		return minBufferPoolHitRatio;
	}

	public void setMinBufferPoolHitRatio(double minBufferPoolHitRatio) {
		this.minBufferPoolHitRatio = minBufferPoolHitRatio;
	}

	public double getAvgBufferPoolHitRatio() {
		return avgBufferPoolHitRatio;
	}

	public void setAvgBufferPoolHitRatio(double avgBufferPoolHitRatio) {
		this.avgBufferPoolHitRatio = avgBufferPoolHitRatio;
	}

	public int getBufferPoolId() {
		return bufferPoolId;
	}

	public void setBufferPoolId(int bufferPoolId) {
		this.bufferPoolId = bufferPoolId;
	}

	public String getBufferPoolName() {
		return bufferPoolName;
	}

	public void setBufferPoolName(String bufferPoolName) {
		this.bufferPoolName = bufferPoolName;
	}

	public int getMissedThresholdCount() {
		return missedThresholdCount;
	}

	public void setMissedThresholdCount(int missedThresholdCount) {
		this.missedThresholdCount = missedThresholdCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public double getMissedThresholdPercentage() {
		return Double.valueOf((missedThresholdCount/rowCount)*100);
	}

}
