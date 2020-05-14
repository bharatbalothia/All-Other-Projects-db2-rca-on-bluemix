package com.ibm.db2.rca.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.ibm.db2.rca.spring.jdbc.ConfigurableRowMapper;
import com.ibm.db2.rca.spring.mvc.CustomerSession;


public abstract class BaseDaoImpl
{	

	@Autowired
	private CustomerSession customerSession = null;
	
	private @Value("${schemaHook}") String schemaHook;
	
	protected @Value("${dynamicSelectPhraseFragmentFromMethod}") String dynamicSelectPhraseFragmentFromMethod;
	
	private Map<String, DaoMethodConfig> daoMethods = null;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	private SimpleJdbcInsert simpleJdbcInsert = null;
		
	public BaseDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
	{
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public BaseDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, SimpleJdbcInsert simpleJdbcInsert)
	{
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		
		this.simpleJdbcInsert = simpleJdbcInsert;
	}
	
	
	public Map<String, DaoMethodConfig> getDaoMethods() {
		return this.daoMethods;
	}

	public void setDaoMethods(Map<String, DaoMethodConfig> daoMethods) {
		this.daoMethods = daoMethods;
	}
	
	public String getSchemaPrefixedSql(String sql, String schemaName)
	{
		return sql.replace(this.schemaHook, schemaName);
	}



	protected MapSqlParameterSource mapSqlParameters(DaoMethodConfig daoMethodConfig, Map<String, Object> values)
	{
		List<String> sqlParams = daoMethodConfig.getSqlParams();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		Object value = null;
		
		if(sqlParams != null)
		{
			for(String sqlParamName : sqlParams)
			{
				value = values.get(sqlParamName);
				
				namedParameters.addValue(sqlParamName, value);			
			}
		}
		
		return namedParameters;
	}
	
	protected Object select(DaoMethodConfig daoMethodConfig, Map<String, Object> sqlParamValues, String schemaName) throws Exception
	{
		String sql = daoMethodConfig.getSql();
		
		if(schemaName != null && schemaName.trim().length() > 0)
		{
			sql = this.getSchemaPrefixedSql(sql, schemaName);
		}
		
		Object object = null;
		
		if(daoMethodConfig.getCardinality() == BaseDao.Cardinality.RnCn)
		{
			object = this.namedParameterJdbcTemplate.query(sql, this.mapSqlParameters(daoMethodConfig,sqlParamValues), new ConfigurableRowMapper(daoMethodConfig));
		}
		else if(daoMethodConfig.getCardinality() == BaseDao.Cardinality.R1Cn)
		{
			object = this.namedParameterJdbcTemplate.queryForObject(sql, this.mapSqlParameters(daoMethodConfig,sqlParamValues), new ConfigurableRowMapper(daoMethodConfig));		
		}
		else if(daoMethodConfig.getCardinality() == BaseDao.Cardinality.RnC1)
		{
			object = this.namedParameterJdbcTemplate.queryForList(sql, this.mapSqlParameters(daoMethodConfig,sqlParamValues), Class.forName(daoMethodConfig.getClassNameOfObjectsReturned()));		
		}
		else if(daoMethodConfig.getCardinality() == BaseDao.Cardinality.R1C1)
		{
			object = this.namedParameterJdbcTemplate.queryForObject(sql, this.mapSqlParameters(daoMethodConfig,sqlParamValues), Class.forName(daoMethodConfig.getClassNameOfObjectsReturned()));		
		}

		return object;
	}
	
	public void insert(DaoMethodConfig daoMethodConfig, Map<String, Object> sqlParamValues) 
	{
		if(this.simpleJdbcInsert.isCompiled() == false)
		{
			List<String> columns = org.springframework.util.CollectionUtils.arrayToList(daoMethodConfig.getInsertSqlColumnsAndBeanPropertiesMap().keySet().toArray());
			
			this.simpleJdbcInsert.withSchemaName(daoMethodConfig.getSchemaName()).withTableName(daoMethodConfig.getTableName()).setColumnNames(columns);
		}
				
		this.simpleJdbcInsert.execute(sqlParamValues);				
	}
	
	protected boolean exist(DaoMethodConfig daoMethodConfig, Map<String, Object> sqlParamValues, String schemaName) throws Exception
	{
		Integer count = (Integer) select(daoMethodConfig, sqlParamValues, schemaName);
		
		if(count  != null)
		{
			if(count > 0)
			{
				return true;
			}			
		}
		
		return false;		
	}	
	
	protected List<?> selectList(DaoMethodConfig daoMethodConfig, Map<String, Object> sqlParamValues, String schemaName) throws Exception
	{
		return (List<?>) select(daoMethodConfig, sqlParamValues, schemaName);
	}
	
	public static List<String> getColumnNames(List<Map<String, Object>> data)
	{
		List<String> columnNameList = null;
		
		if(data!=null)
		{
			if(data.size() > 0)
			{
				Map<String, Object> row = data.get(0);
				
				columnNameList = org.springframework.util.CollectionUtils.arrayToList(row.keySet().toArray());
			}
		}
		
		return columnNameList;
	}
	
	public static double[] getArray(List<Map<String, Object>> data, String columnName)
	{
		double [] dataArray = new double [data.size()];
		
		Map< String, Object> row = null;
		Object columnValue = null;
		for(int i = 0 ; i < data.size(); i++)
		{
			row = data.get(i);
			
			columnValue = row.get(columnName);
			
			if(columnValue instanceof java.math.BigDecimal)
			{
				dataArray[i] = ((BigDecimal)row.get(columnName)).doubleValue();
			}
			else if(columnValue instanceof java.lang.Long)
			{
				dataArray[i] = ((Long)row.get(columnName)).doubleValue();
			}
			else if(columnValue instanceof java.lang.Double)
			{
				dataArray[i] = ((Double)row.get(columnName)).doubleValue();
			}
				
			
			
		}
		
		return dataArray;
		
	}
	
	
	protected Object selectObject(DaoMethodConfig daoMethodConfig, Map<String, Object> sqlParamValues, String schemaName) throws Exception
	{
		return select(daoMethodConfig, sqlParamValues, schemaName);
	}
	
	protected double selectDoubleValue(DaoMethodConfig daoMethodConfig, Map<String, Object> sqlParamValues, String schemaName) throws Exception
	{
		return (Double)select(daoMethodConfig, sqlParamValues, schemaName);
	}
	
	protected HashMap selectHashMap(DaoMethodConfig daoMethodConfig, Map<String, Object> sqlParamValues, String schemaName) throws Exception
	{
		return (HashMap)select(daoMethodConfig, sqlParamValues, schemaName);
	}
	
/*	protected void makeUserSpecificQuery(DaoMethodConfig daoMethodConfig, String schemaName)
	{
		if(daoMethodConfig !=null)
		{
			String genericSql = daoMethodConfig.getSql();
			
			if(genericSql != null && genericSql.trim().length() > 0)
			{
				this.userSqls.put(genericSql, genericSql.replace(this.schemaHook,schemaName));
			}
		}		
	}*/
	

//	public List<?> selectRnCnObjects(DaoMethodConfig daoMethodConfig) throws DataAccessException, ClassNotFoundException 
//	{			
//		return this.namedParameterJdbcTemplate.query(daoMethodConfig.getSql(), this.mapSqlParameters(daoMethodConfig), new ConfigurableRowMapper(daoMethodConfig));
//	}
//	
//
//	public List<?> selectRnCn(Map<String,Object> params) throws DataAccessException, ClassNotFoundException 
//	{
//		String sql = null;
//		
//		Map<String, Object>  methodBeanConfig = (Map<String, Object>) params.get("methodBeanConfig");
//		
//		sql = (String) params.get("sql");
//		
//		List<String> sqlParams = (List<String>) methodBeanConfig.get("sqlParams");
//		
//		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
//		
//		if(sqlParams != null)
//		{
//			for(String sqlParamName : sqlParams)
//			{
//				namedParameters.addValue(sqlParamName, params.get(sqlParamName));			
//			}
//		}			
//		return this.namedParameterJdbcTemplate.queryForList(sql, namedParameters);
//	}
//	
//
//	public Object selectR1C1Object(Map<String,Object> params) throws DataAccessException, ClassNotFoundException 
//	{
//		String sql = null;
//		
//		Map<String, Object>  methodBeanConfig = (Map<String, Object>) params.get("methodBeanConfig");
//		
//		sql = (String) params.get("sql");
//		
//		List<String> sqlParams = (List<String>) methodBeanConfig.get("sqlParams");
//		
//		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
//		
//		if(sqlParams != null)
//		{		
//			for(String sqlParamName : sqlParams)
//			{
//				namedParameters.addValue(sqlParamName, params.get(sqlParamName));			
//			}
//		}
//			
//		return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Object.class);
//	}
//	
//	
//	public double selectR1C1Double(Map<String,Object> params) throws DataAccessException, ClassNotFoundException 
//	{			
//		return (Double) this.selectR1C1Object(params);
//	}
}
