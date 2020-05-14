package com.ibm.db2.rca.dao;

import java.util.List;
import java.util.Map;

import com.ibm.db2.rca.util.Utils;

public class DaoMethodConfig
{		
	public DaoMethodConfig() 
	{
		super();
	}

	private String sql = null;
	
	private String tableName = null;
	
	private String schemaName = null;
	
	private List<String> sqlParams = null;
	
	private List<String> listOfOutputSqlColumns = null;
	
	private Map<String,String> insertSqlColumnsAndBeanPropertiesMap = null;
	
	private Map<String,String> classPropertiesSqlColumnMapping = null;
	
	private String classNameOfObjectsReturned = null;
	
	private BaseDao.Cardinality cardinality = null;

	
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<String> getSqlParams() {
		return sqlParams;
	}

	public void setSqlParams(List<String> sqlParams) {
		this.sqlParams = sqlParams;
	}

	public List<String> getListOfOutputSqlColumns() {
		return listOfOutputSqlColumns;
	}

	public void setListOfOutputSqlColumns(List<String> listOfOutputSqlColumns) {
		this.listOfOutputSqlColumns = listOfOutputSqlColumns;
	}

	public Map<String, String> getClassPropertiesSqlColumnMapping() {
		return classPropertiesSqlColumnMapping;
	}

	public void setClassPropertiesSqlColumnMapping(
			Map<String, String> classPropertiesSqlColumnMapping) {
		this.classPropertiesSqlColumnMapping = classPropertiesSqlColumnMapping;
	}

	public String getClassNameOfObjectsReturned() {
		return classNameOfObjectsReturned;
	}

	public void setClassNameOfObjectsReturned(String classNameOfObjectsReturned) {
		this.classNameOfObjectsReturned = classNameOfObjectsReturned;
	}

	public BaseDao.Cardinality getCardinality() {
		return cardinality;
	}

	public void setCardinality(BaseDao.Cardinality cardinality) {
		this.cardinality = cardinality;
	}		
	


	public Map<String,String> getInsertSqlColumnsAndBeanPropertiesMap() {
		return insertSqlColumnsAndBeanPropertiesMap;
	}

	public void setInsertSqlColumnsAndBeanPropertiesMap(
			Map<String,String> insertSqlColumnsAndBeanPropertiesMap) {
		this.insertSqlColumnsAndBeanPropertiesMap = insertSqlColumnsAndBeanPropertiesMap;
	}

	public void validate() throws BaseDaoException
	{
		if(this.classPropertiesSqlColumnMapping != null)					
		{
			if(Utils.isEmpty(this.classNameOfObjectsReturned) && this.classPropertiesSqlColumnMapping.size() > 0)				
			{				
				throw new BaseDaoException("If classPropertiesSqlColumnMapping property is defined then a valid Class must be defined in classNameOfObjectsReturned property.");
			}											
		}			
		
		if(Utils.isEmpty(this.classNameOfObjectsReturned) == false)
		{
			try 
			{
				Class.forName(this.classNameOfObjectsReturned);
			} 
			catch (ClassNotFoundException e)
			{					
				e.printStackTrace();					
				throw new BaseDaoException("No such class " + this.classNameOfObjectsReturned + " is present.", e);					
			}
		}
		
	}
	
}