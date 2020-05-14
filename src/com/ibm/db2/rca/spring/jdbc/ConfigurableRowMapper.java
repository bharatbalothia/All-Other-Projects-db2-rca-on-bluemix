package com.ibm.db2.rca.spring.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.jdbc.core.RowMapper;

import com.ibm.db2.rca.dao.DaoMethodConfig;


public class ConfigurableRowMapper implements RowMapper<Object> {

	private DaoMethodConfig daoMethodConfig = null;
	
	public ConfigurableRowMapper(DaoMethodConfig daoMethodConfig) 
	{
		this.daoMethodConfig = daoMethodConfig;
	}

	
	private Object mapRowFilteredColumnsToObjectProperties(ResultSet rs) throws Exception
	{
		Object object = null;
		
		Iterator<String> itr = this.daoMethodConfig.getListOfOutputSqlColumns().iterator();
		
		String columnName = null;
		
		Object columnValue = null;

		String classPropertyName = null;

		BeanWrapper wrapper = null;
		
		try 
		{
			wrapper = new BeanWrapperImpl(Class.forName(daoMethodConfig.getClassNameOfObjectsReturned()));
		} 
		catch (ClassNotFoundException e) 
		{				
			//TODO
			throw new Exception ("Class Not found", e);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		while(itr.hasNext())
		{
			classPropertyName = null;
			
			columnName = itr.next();			
			
			if(this.daoMethodConfig.getClassPropertiesSqlColumnMapping().containsKey(columnName))
			{
				classPropertyName = this.daoMethodConfig.getClassPropertiesSqlColumnMapping().get(columnName);
			}
			
			columnValue = rs.getObject(columnName.trim());
			
			map.put(classPropertyName, columnValue);
			
		}

		wrapper.setPropertyValues(map);

		object = wrapper.getWrappedInstance();
				
		return object;
		
	}

	private Object mapRowColumnsToHashMap(ResultSet rs) throws SQLException
	{
		ResultSetMetaData metaData = rs.getMetaData();
		
		int colCount = metaData.getColumnCount();

		Map<String, Object> columns = new HashMap<String, Object>(colCount);
		
		for (int i = 1; i <= colCount; i++) 
		{
		    columns.put(metaData.getColumnLabel(i), rs.getObject(i));
		}

		return columns;
	}
	
	private Object mapRowColumnsToObjectProperties(ResultSet rs) throws Exception
	{
		Object object = null;
		
		Iterator<Entry<String, String>> itr = this.daoMethodConfig.getClassPropertiesSqlColumnMapping().entrySet().iterator();

		Entry<String, String> entry = null; 
		
		String columnName = null;
		
		Object columnValue = null;
		
		String classPropertyName = null;
  
		BeanWrapper wrapper = null;

		wrapper = new BeanWrapperImpl(Class.forName(this.daoMethodConfig.getClassNameOfObjectsReturned()));				
  
		Map<String,Object> map = new HashMap<String,Object>();

		while(itr.hasNext())
		{
		  entry = itr.next();
		  
		  columnName = entry.getKey();
		  
		  columnValue = rs.getObject(columnName.trim());
		  
		  classPropertyName = entry.getValue();
		  
		  map.put(classPropertyName, columnValue);
		}
  
		wrapper.setPropertyValues(map);
	  
		object = wrapper.getWrappedInstance();

		return object;
		
	}
	
	
	private Object mapRowFilteredColumnsToNativeDataTypes(ResultSet rs) throws Exception
	{
		
		Iterator<String> itr = this.daoMethodConfig.getListOfOutputSqlColumns().iterator();
		
		String columnName = null;
		
		Object columnValue = null;

		Map<String, Object> map = new HashMap<String, Object>();
		
		while(itr.hasNext())
		{
			columnValue = null;
			
			columnName = itr.next();			
			
			columnValue = rs.getObject(columnName.trim());
			
			map.put(columnName, columnValue);
			
		}
				
		return map;
		
	}
	
	
	@Override
	public Object mapRow(ResultSet rs, int line) throws SQLException 
	{

		Object object = null;
		
		if(this.daoMethodConfig.getClassPropertiesSqlColumnMapping() != null 
				&& this.daoMethodConfig.getClassNameOfObjectsReturned() != null 
				&& this.daoMethodConfig.getListOfOutputSqlColumns() != null)
		{
			try {
				object = this.mapRowFilteredColumnsToObjectProperties(rs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else if(this.daoMethodConfig.getClassPropertiesSqlColumnMapping() == null 
				&& this.daoMethodConfig.getClassNameOfObjectsReturned() == null 
				&& this.daoMethodConfig.getListOfOutputSqlColumns() != null)	
		{
			try {
				object = this.mapRowFilteredColumnsToNativeDataTypes(rs);								
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(this.daoMethodConfig.getClassPropertiesSqlColumnMapping() != null 
				&& this.daoMethodConfig.getClassNameOfObjectsReturned() != null 
				&& this.daoMethodConfig.getListOfOutputSqlColumns() == null)
		{
			try {
				object = this.mapRowColumnsToObjectProperties(rs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try {
				object = this.mapRowColumnsToHashMap(rs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		return object;

	}

}
