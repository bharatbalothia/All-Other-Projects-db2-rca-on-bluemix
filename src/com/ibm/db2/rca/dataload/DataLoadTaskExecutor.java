package com.ibm.db2.rca.dataload;

import java.util.concurrent.Future;

import org.springframework.core.task.AsyncTaskExecutor;

public class DataLoadTaskExecutor {
	
	private AsyncTaskExecutor asyncTaskExecutor;

    public DataLoadTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) 
    {
        this.asyncTaskExecutor = asyncTaskExecutor;
    }
    
    public Future<String> executeDataLoad()
    {
    	Future<String> future;
    	
    	future = (Future<String>) this.asyncTaskExecutor.submit(new LoadDataFromDelimitedFile());
    	
    	return future;
    }
    

}
