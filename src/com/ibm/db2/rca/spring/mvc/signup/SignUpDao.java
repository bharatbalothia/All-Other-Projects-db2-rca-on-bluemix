package com.ibm.db2.rca.spring.mvc.signup;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.ibm.db2.rca.dao.BaseDaoImpl;
import com.ibm.db2.rca.dao.DaoMethodConfig;


public class SignUpDao extends BaseDaoImpl
{
	
	List<String> dbColumnNames;
	
	public SignUpDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, SimpleJdbcInsert simpleJdbcInsert) 
	{
		super(namedParameterJdbcTemplate, simpleJdbcInsert);		
		
	}

	
	public void insertUserRecord(SignUpInputToService signUpInputToService) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();
		
		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);
		
		
		Map<String,String> insertSqlColumnsAndBeanPropertiesMap = daoMethodConfig.getInsertSqlColumnsAndBeanPropertiesMap();		
		
		if(insertSqlColumnsAndBeanPropertiesMap != null && insertSqlColumnsAndBeanPropertiesMap.size() > 0)		
		{
			
			Iterator<String> itr = insertSqlColumnsAndBeanPropertiesMap.keySet().iterator();
			
			String columnName ;
			
			while (itr.hasNext())
			{
				columnName = itr.next();
				
				sqlParamValues.put(columnName, PropertyUtils.getProperty(signUpInputToService, insertSqlColumnsAndBeanPropertiesMap.get(columnName)));
					
			}		
		}
		
		super.insert(daoMethodConfig, sqlParamValues);

	}
	
	public boolean queryUserExistence(SignUpInputToService signUpInputToService) throws Exception
	{
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/
				
		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);		
		
		List <String> params = daoMethodConfig.getSqlParams();
		
		if(params != null && params.size() > 0)		
		{
			
			Iterator<String> itr = params.iterator();
			
			String param ;
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				sqlParamValues.put(param, PropertyUtils.getProperty(signUpInputToService, param));					
			}		
		}			
		
		return super.exist(daoMethodConfig, sqlParamValues, "RCACONF");
		
	}
	

}
