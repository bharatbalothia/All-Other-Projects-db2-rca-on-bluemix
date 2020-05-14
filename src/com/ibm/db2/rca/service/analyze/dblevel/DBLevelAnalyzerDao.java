package com.ibm.db2.rca.service.analyze.dblevel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ibm.db2.rca.dao.BaseDaoImpl;
import com.ibm.db2.rca.dao.DaoMethodConfig;
import com.ibm.db2.rca.spring.mvc.CustomerSession;
import com.ibm.db2.rca.util.JsonCloner;


public class DBLevelAnalyzerDao extends BaseDaoImpl
{
	@Autowired
	private CustomerSession customerSession = null;
	
	
	@Resource(name="metricBucketRelation_DbLevel")
	private Map<String,String> metricBucketRelation_DbLevel = null;
	
	@Resource(name="bucketNameList")
	private List<String> bucketNameList = null;
	
	public DBLevelAnalyzerDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) 
	{
		super(namedParameterJdbcTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryMonitoredDataForAllComponents_LevelDatabase() throws Exception 
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
		
		
		//System.out.println(this.getClass().getSimpleName() + " : Object ID : " + this.hashCode());
		
		return (List<Map<String, Object>>) super.selectList(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
	}
	
	
	public List<Map<String, Object>> queryMonitoredDataForAllComponents_LevelDatabase(List<String> listOfOutputSqlColumns) throws Exception 
	{	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig clonnedDaoMethodConfig = JsonCloner.clone((DaoMethodConfig) this.getDaoMethods().get(methodName));
		
		clonnedDaoMethodConfig.setListOfOutputSqlColumns(listOfOutputSqlColumns);
		
		List <String> params = clonnedDaoMethodConfig.getSqlParams();		
		
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
		
		
		//System.out.println(this.getClass().getSimpleName() + " : Object ID : " + this.hashCode());
		
		return (List<Map<String, Object>>) super.selectList(clonnedDaoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryMonitoredDataForAllComponentsBucketize_LevelDatabase() throws Exception 
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
		
		
		//System.out.println(this.getClass().getSimpleName() + " : Object ID : " + this.hashCode());
		
		return (List<Map<String, Object>>) super.selectList(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryMonitoredDataForGivenMetricsBucketize_LevelDatabase(List<String> listOfOutputSqlColumns) throws Exception 
	{	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig clonnedDaoMethodConfig = JsonCloner.clone((DaoMethodConfig) this.getDaoMethods().get(methodName));
		
		Map<String,String> selectClausePhrases = new HashMap<String,String>();
		
		String selectClausePhrase = null, bucketName =null;
		
		for(String metricName :  listOfOutputSqlColumns)
		{
			if(metricName.compareTo("TS") == 0 || metricName.compareTo("TOTAL_RQST_TIME") == 0)
			{
				continue;
			}
			
			bucketName = this.metricBucketRelation_DbLevel.get(metricName);
			
			selectClausePhrase = selectClausePhrases.get(bucketName);
			
			if(selectClausePhrase != null)
			{
				selectClausePhrase = selectClausePhrase + " + " +metricName;
			}
			else
			{
				selectClausePhrase = metricName;
			}
			
			selectClausePhrases.put(bucketName, selectClausePhrase);			
		}
		

		StringBuilder selectPhraseBuilder = new StringBuilder();
		
		for(String bucketNameFromBucketList :  this.bucketNameList)
		{			
			selectClausePhrase = selectClausePhrases.get(bucketNameFromBucketList);
			
			if(selectClausePhrase == null)
			{
				continue;
			}
			
			if(selectClausePhrase.lastIndexOf('+') >= 0)
			{
				selectClausePhrase = selectClausePhrase.substring(0,selectClausePhrase.lastIndexOf('+')-1);
			}
			
			
			
			selectClausePhrase = " dec( " + selectClausePhrase +  " ) / dec(TOTAL_APP_COMMITS) as " + bucketNameFromBucketList + " , ";
			
			selectPhraseBuilder.append(selectClausePhrase);
		}
		
		String selectPhrase = selectPhraseBuilder.toString();
		
		selectPhrase.substring(0,selectPhrase.lastIndexOf(',')+1);
		
		
		String sql = clonnedDaoMethodConfig.getSql();
		
		sql = sql.replace(super.dynamicSelectPhraseFragmentFromMethod, selectPhrase);
		
		clonnedDaoMethodConfig.setSql(sql);
		
		Iterator <String> itr = selectClausePhrases.keySet().iterator();
		
		while(itr.hasNext())
		{
			clonnedDaoMethodConfig.getListOfOutputSqlColumns().add(itr.next());
		}
		
		List <String> params = clonnedDaoMethodConfig.getSqlParams();	
		
		
		if(params != null && params.size() > 0)		
		{
			
			itr = params.iterator();
			
			String param ;
			
			while (itr.hasNext())
			{
				param = itr.next();
				
				sqlParamValues.put(param, PropertyUtils.getProperty(this.customerSession.getInputToService(), param));
					
			}		
		}	
		
		
		//System.out.println(this.getClass().getSimpleName() + " : Object ID : " + this.hashCode());
		
		return (List<Map<String, Object>>) super.selectList(clonnedDaoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
	}
	
	public Map<String, BigDecimal> queryMaxPercentageContributionOfAllComponentsToTotalRequestTime_LevelDatabase() throws Exception 
	{	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/
				
		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println(methodName + " called.");

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
		
		return (Map<String, BigDecimal>) super.selectHashMap(daoMethodConfig, sqlParamValues, this.customerSession.getCustomerID());
	}
	
	
}
