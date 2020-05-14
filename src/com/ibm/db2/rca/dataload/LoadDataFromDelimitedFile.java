package com.ibm.db2.rca.dataload;

import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.sql.DataSource;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.util.Assert;

import com.ibm.db2.rca.util.Utils;


public class LoadDataFromDelimitedFile implements Callable
{
	private char delimiter = 0; 
	
	private DataSource dataSource = null;
	
	private Map<String, Map<String, String>> dataLoadDescriptors = null;
	
	private ConversionService conversionService = null;
	
	private SimpleJdbcInsert simpleJdbcInsert;

	private int BATCH_SIZE = 100; 
	
	private TaskExecutor taskExecutor = null;
	
	public static final String CSV_COLUMN_DATA_TYPE_CLASS_NAME = "csvColumnDataTypeClassName";
	
	public static final String CSV_COLUMN_FORMAT = "csvColumnFormat";
	
	public static final String DB_COLUMN_NAME = "dbColumnName";
	
	public static final String DB_COLUMN_DATA_TYPE_CLASS_NAME = "dbColumnDataTypeClassName";
			
	public static final String DB_COLUMN_FORMAT = "dbColumnFormat";
	
	public static final String JAVA_UTIL_DATE_CLASS_NAME = "java.util.Date";
	
	public void setDataSource(DataSource dataSource) 
	{
		this.dataSource = dataSource;  		
		
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		
	}
	
    public LoadDataFromDelimitedFile(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
	
	public void setDataLoadDescriptors(Map<String, Map<String, String>> dataLoadDescriptors) 
	{
		this.dataLoadDescriptors = dataLoadDescriptors;
	}

	public void setConversionService(ConversionService conversionService) 
	{
		this.conversionService = conversionService;
	}

	public LoadDataFromDelimitedFile()
	{

	}
	
	public boolean parseAndLoadIntoDbTable(String targetTablename, String targetSchemaName, String sourceFilename) throws Exception
	{
		JdbcTemplate jdbcTemplate =  new JdbcTemplate(this.dataSource);
		
		String truncateSqlStatement = "TRUNCATE TABLE " + targetSchemaName + '.' + targetTablename + " IMMEDIATE";
		
		jdbcTemplate.execute(truncateSqlStatement);

		
		boolean status = true;
		
		int recordCount = 0;
		
		CSVFormat format = CSVFormat.RFC4180;
		
		format = format.withHeader();
			
		format.withDelimiter(this.delimiter);
		
		CSVParser parser = new CSVParser(new FileReader(sourceFilename), format);

		Map<String, Object> parameters = null;
		
		List <Map<String, Object>> batch = new ArrayList<Map<String, Object>>(); 
		
		List<String> dbColumnNames = this.getDbColumnNames();
		
		this.simpleJdbcInsert.withSchemaName(targetSchemaName).withTableName(targetTablename).setColumnNames(dbColumnNames);
		
		for(CSVRecord record : parser)
		{
			parameters = new HashMap<String, Object>();
			
			Iterator csvColumnNames = this.dataLoadDescriptors.keySet().iterator();
			
			while(csvColumnNames.hasNext())
			{
				String csvColumnName = (String) csvColumnNames.next();
				
				Class dbColumnDataTypeClass = null; 
				
				String csvColumnValue = record.get(csvColumnName);
				
				if(!Utils.isEmpty(csvColumnValue))
				{
					csvColumnValue = csvColumnValue.trim();
				}
				
				Map<String, String> dataLoadDescriptor = dataLoadDescriptors.get(csvColumnName);
				
				String csvColumnDataTypeClassName = dataLoadDescriptor.get(CSV_COLUMN_DATA_TYPE_CLASS_NAME);
				
				if(!Utils.isEmpty(csvColumnDataTypeClassName))
				{
					csvColumnDataTypeClassName = csvColumnDataTypeClassName.trim();
				}
				
				String csvColumnFormat = dataLoadDescriptor.get(CSV_COLUMN_FORMAT);
				
				if(!Utils.isEmpty(csvColumnFormat))
				{
					csvColumnFormat = csvColumnFormat.trim();
				}

				String dbColumnName = (String) dataLoadDescriptor.get(DB_COLUMN_NAME);
				
				if(Utils.isEmpty(dbColumnName))
				{
					dbColumnName = csvColumnName.trim();
				}
				else
				{
					dbColumnName = dbColumnName.trim();
				}
				
				csvColumnName = csvColumnName.trim(); 
				
				String dbColumnDataTypeClassName = dataLoadDescriptor.get(DB_COLUMN_DATA_TYPE_CLASS_NAME);
				
				if(!Utils.isEmpty(dbColumnDataTypeClassName))
				{
					dbColumnDataTypeClassName = dbColumnDataTypeClassName.trim();
					
					dbColumnDataTypeClass = Class.forName(dbColumnDataTypeClassName);
				}
				
				String dbColumnFormat = dataLoadDescriptor.get(DB_COLUMN_FORMAT);
				
				if(!Utils.isEmpty(dbColumnFormat))
				{
					dbColumnFormat = dbColumnFormat.trim();
				}
				


				if(!Utils.isEmpty(csvColumnDataTypeClassName))
				{
					if(Class.forName(csvColumnDataTypeClassName) == java.util.Date.class &&
							(dbColumnDataTypeClass == java.sql.Date.class || dbColumnDataTypeClass == java.sql.Timestamp.class))
					{

						if(Utils.isEmpty(csvColumnFormat))
						{
							throw new Exception(CSV_COLUMN_DATA_TYPE_CLASS_NAME + " property is present but " 
											+ CSV_COLUMN_FORMAT
											+ " property is missing while defining load descriptor in Spring beans."
									);														
						}

						
						Timestamp ts = Utils.parseDb2ExportedTimestamp(csvColumnValue);
						
						
						//SimpleDateFormat dateFormat = new SimpleDateFormat(csvColumnFormat);
						
						//Date parsedDate = dateFormat.parse(csvColumnValue);
							
						Assert.notNull(ts, "CSV Date Parsing has been failed.");

						
						if(ts != null)
						{
							parameters.put(dbColumnName, ts);
							
						}

					}
					else if(dbColumnDataTypeClass == java.lang.String.class)
					{
						parameters.put(dbColumnName, csvColumnValue);
					}

				}
				else
				{					
					parameters.put(dbColumnName, this.conversionService.convert(csvColumnValue, Long.class));
				}

			}
			
			batch.add(parameters);
			
			recordCount ++;
			
			if(recordCount % BATCH_SIZE > 0)
			{

				@SuppressWarnings("rawtypes")
				Map [] values = batch.toArray(new Map[batch.size()]);
				
				@SuppressWarnings("unchecked")
				int result [] = this.simpleJdbcInsert.executeBatch(values);
				
				if(! getReturnStatus(result) )
				{
					status = false;

				}
				
				batch = new ArrayList<Map<String, Object>>();				
			}
        }
		
		if(recordCount % BATCH_SIZE > 0)
		{
			
			@SuppressWarnings("rawtypes")
			Map [] values = batch.toArray(new Map[batch.size()]);
			
			@SuppressWarnings("unchecked")
			int result [] = this.simpleJdbcInsert.executeBatch(values);
			
			if(! getReturnStatus(result) )
			{
				status = false;

			}
			
			batch = new ArrayList<Map<String, Object>>();				
		}
		
		return status;
	}
	
	private boolean calculateDelta()
	{
		boolean success = true;
		
		
		
		
		return success;
	}
	
	private List<String> getDbColumnNames()
	{
		Iterator<String> itr =  this.dataLoadDescriptors.keySet().iterator();
		String csvColumnName = null;
		String dbColumnName = null;
		Map<String,String> columnDescriptor = null;
		List<String> dbColumnNames = new ArrayList<String>();
		while(itr.hasNext())
		{
			csvColumnName = itr.next();
			columnDescriptor = this.dataLoadDescriptors.get(csvColumnName);
			dbColumnName = columnDescriptor.get(DB_COLUMN_NAME);			
			if(Utils.isEmpty(dbColumnName))
			{
				dbColumnName = csvColumnName;
			}
			else
			{
				dbColumnName = dbColumnName.trim();
			}
			dbColumnNames.add(dbColumnName);			
		}
		return dbColumnNames;
	}
	
	private boolean getReturnStatus(int [] returnCodeArray)
	{
		boolean status = true;
		
		for(int returnCode : returnCodeArray)
		{
			if(returnCode != 0)
			{
				status = false;
				
				break;				
			}
		}
		
		return status;
	}
	
    public static void main(String[] args) throws Exception {
        
        @SuppressWarnings("resource")
		ApplicationContext applicationContext = 
        		new FileSystemXmlApplicationContext("file:///C:/Users/IBM_ADMIN/workspace/RcaOnBluemix/WebContent/WEB-INF/spring/app/csv-beans.xml");

        LoadDataFromDelimitedFile dataLoad = (LoadDataFromDelimitedFile) applicationContext.getBean("dataLoad");
      
        dataLoad.parseAndLoadIntoDbTable("MGW_REPOS", "TEST", "C:\\data\\db2_rca\\sample_data\\MGW_REPOS.csv");
    }


	@Override
	public String call() throws Exception {
		String result = null;
		return result;
	}
}
