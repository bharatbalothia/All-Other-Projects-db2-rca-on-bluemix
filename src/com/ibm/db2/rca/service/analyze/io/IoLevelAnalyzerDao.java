package com.ibm.db2.rca.service.analyze.io;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ibm.db2.rca.dao.BaseDaoImpl;
import com.ibm.db2.rca.dao.DaoMethodConfig;
import com.ibm.db2.rca.spring.mvc.CustomerSession;
import com.ibm.db2.rca.util.JsonCloner;


public class IoLevelAnalyzerDao extends BaseDaoImpl
{
	@Autowired
	private CustomerSession customerSession = null;
	
	private @Value("${queryFragmentBufferPoolReadTimeForAGivenPool_LevelBufferPool}") String queryFragmentBufferPoolReadTimeForAGivenPool_LevelBufferPool;
	
	private @Value("${queryFragmentBufferPoolReadTime_LevelDatabase}") String queryFragmentBufferPoolReadTime_LevelDatabase;	
	
	private @Value("${poolNameHook}") String poolNameHook;
	
	
	public IoLevelAnalyzerDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) 
	{
		super(namedParameterJdbcTemplate);
	}

	@SuppressWarnings("unchecked")
	
	
	
	
	public double getMaxPercentageSumPoolReadTime() throws Exception 
	{	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);

		List <String> params = daoMethodConfig.getSqlParams();		
		
		if(params != null && params.size() > 0)		
		{
			
			Iterator<String> itr = params.iterator();
			
			String param ;
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				sqlParamValues.put(param, PropertyUtils.getProperty(this.customerSession.getInputToService(), param));
					
			}		
		}	

		return super.selectDoubleValue(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
	}
	
	
	public double getMaxReadResponseTime() throws Exception 
	{
	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);

		List <String> params = daoMethodConfig.getSqlParams();		
		
		if(params != null && params.size() > 0)		
		{
			
			Iterator<String> itr = params.iterator();
			
			String param ;
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				sqlParamValues.put(param, PropertyUtils.getProperty(this.customerSession.getInputToService(), param));
					
			}		
		}	

		return super.selectDoubleValue(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
		
	}
	
	public List<Map<String, Object>> getPoolReadTimeForAGivenPoolAlongwithDbLevelPoolReadTime(String bpName) throws Exception 
	{
	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);

		List <String> params = daoMethodConfig.getSqlParams();		
		
		if(params != null && params.size() > 0)		
		{
			
			Iterator<String> itr = params.iterator();
			
			String param ;
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				sqlParamValues.put(param, PropertyUtils.getProperty(this.customerSession.getInputToService(), param));
					
			}
			
			sqlParamValues.put("bpName", bpName);

		}	

		return (List<Map<String, Object>>) super.selectList(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPoolReadTimeForGivenPoolsAlongwithDbLevelPoolReadTime(String sql) throws Exception 
	{
	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);

		daoMethodConfig = JsonCloner.clone(daoMethodConfig);
		
		daoMethodConfig.setSql(sql);
		
		List <String> params = daoMethodConfig.getSqlParams();		
		
		if(params != null && params.size() > 0)		
		{
			
			Iterator<String> itr = params.iterator();
			
			String param ;
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				sqlParamValues.put(param, PropertyUtils.getProperty(this.customerSession.getInputToService(), param));
					
			}			
		}	

		return (List) super.selectList(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
		
	}
	
	public List<Map<String, Object>> getRowsReadVsRowsReturnRatioDataSet() throws Exception 
	{
	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);

		List <String> params = daoMethodConfig.getSqlParams();		
		
		if(params != null && params.size() > 0)		
		{
			
			Iterator<String> itr = params.iterator();
			
			String param ;
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				sqlParamValues.put(param, PropertyUtils.getProperty(this.customerSession.getInputToService(), param));
					
			}		
		}	

		return (List<Map<String, Object>>) super.selectList(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
		
	}
	
	
	public List<String> queryBufferPoolNameList() throws Exception
	{
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);

		List <String> params = daoMethodConfig.getSqlParams();		
		
		if(params != null && params.size() > 0)		
		{
			
			Iterator<String> itr = params.iterator();
			
			String param ;
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				sqlParamValues.put(param, PropertyUtils.getProperty(this.customerSession.getInputToService(), param));
					
			}		
		}	

		return (List<String>) super.selectList(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
	}
	
	public String createBufferPoolJoinQueryForReadTime(String[] bufferPoolNameArray)
	{
		
		StringWriter query = new StringWriter(); 
		
		StringWriter selectStatementFragment = new StringWriter();
		
		StringWriter fromStatementFragment = new StringWriter();
		
		
		String queryFragment = null;
		
		String bufferPoolName, prevBufferPoolName = null;
		
		
		bufferPoolName = bufferPoolNameArray[0];
		
		query.append(" with ");
		
		
		queryFragment = this.queryFragmentBufferPoolReadTimeForAGivenPool_LevelBufferPool;
		
		queryFragment = queryFragment.replace(this.poolNameHook, bufferPoolName);
		
		query.append(queryFragment).append(',');
		
		selectStatementFragment.append(" select ");
		
		fromStatementFragment.append(" from ").append(bufferPoolName);

		selectStatementFragment.append(bufferPoolName+"_POOL_READ_TIME, ");
		
		
		for(int i=1; i < bufferPoolNameArray.length; i++)
		{
			prevBufferPoolName = bufferPoolName;
			
			bufferPoolName = bufferPoolNameArray[i];
			
			queryFragment = this.queryFragmentBufferPoolReadTimeForAGivenPool_LevelBufferPool;
			
			queryFragment = queryFragment.replace(this.poolNameHook, bufferPoolName);
			
			query.append(queryFragment);
			
			query.append(',');
			
			selectStatementFragment.append(prevBufferPoolName+"_POOL_READ_TIME, ");
			
			fromStatementFragment.append(" full outer join " + bufferPoolName + " on " + prevBufferPoolName + ".TS = " + bufferPoolName + ".TS ");
		}
		
		query.append(this.queryFragmentBufferPoolReadTime_LevelDatabase);
		
		selectStatementFragment.append(" DB.TS, DB.DB_POOL_READ_TIME ");
		
		fromStatementFragment.append(" full outer join DB on " + bufferPoolName + ".TS = " + "DB.TS ");
		
		query.append(selectStatementFragment.toString()).append(fromStatementFragment.toString());
				
		return query.toString();
	}

	public List<Map<String, Object>> queryAllBufferPoolReadTimeAlongWithDbPoolReadTime_LevelBufferPool() throws Exception {
		
		
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		// Get the List of BufferPool Names
		
		List<String> bufferPoolNameList = this.queryBufferPoolNameList();

		String[] bufferPoolNameArray = new String[bufferPoolNameList.size()];
		
		if(bufferPoolNameArray == null  || bufferPoolNameArray.length == 0 )
			return null;
				
		bufferPoolNameList.toArray(bufferPoolNameArray);
		
		String sql = this.createBufferPoolJoinQueryForReadTime(bufferPoolNameArray);

		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);

		daoMethodConfig = JsonCloner.clone(daoMethodConfig);
		
		daoMethodConfig.setSql(sql);
		
		List <String> params = daoMethodConfig.getSqlParams();		
		
		if(params != null && params.size() > 0)		
		{
			
			Iterator<String> itr = params.iterator();
			
			String param ;
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				sqlParamValues.put(param, PropertyUtils.getProperty(this.customerSession.getInputToService(), param));
					
			}			
		}	

		return (List) super.selectList(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());

	}

	public List<Map<String, Object>> queryBufferPoolReadTimeForGivePoolNamesAlongWithDbPoolReadTime_LevelBufferPool(List<String> listOfColumns) throws Exception 
	{
		String sql = this.createBufferPoolJoinQueryForReadTime((String [])listOfColumns.toArray());
		
		return this.getPoolReadTimeForGivenPoolsAlongwithDbLevelPoolReadTime(sql);
	}

	public Map<String, BigDecimal> queryMaxPercentageContributionOfGivenBufferPoolsToDbLevelPoolReadTime_LevelBufferPool(List<String> bufferPoolNameList) throws Exception {
 
		BigDecimal maxContribution;
		
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>(); 
		
		for(String bufferPoolName : bufferPoolNameList)
		{
			maxContribution = (BigDecimal) this.queryMaxPercentageContributionOfAGivenBufferPoolToDbLevelBufferPoolReadTime_LevelBufferPool(bufferPoolName);
			
			map.put(bufferPoolName, maxContribution);
			
		}
				
		return map;

	}
	
	public Object queryMaxPercentageContributionOfAGivenBufferPoolToDbLevelBufferPoolReadTime_LevelBufferPool(String bufferPoolName) throws Exception 
	{
	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);

		List <String> params = daoMethodConfig.getSqlParams();		
		
		if(params != null && params.size() > 0)		
		{
			
			Iterator<String> itr = params.iterator();
			
			String param ;
			
			
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				try
				{
					sqlParamValues.put(param, PropertyUtils.getProperty(this.customerSession.getInputToService(), param));
				}
				catch (java.lang.NoSuchMethodException e)
				{
					continue;
				}				
			}
			
			sqlParamValues.put("bpName", bufferPoolName);

		}	

		return super.selectObject(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
		
	}
	
	
}
