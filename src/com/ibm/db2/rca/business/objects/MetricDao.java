package com.ibm.db2.rca.business.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ibm.db2.rca.dao.BaseDaoImpl;
import com.ibm.db2.rca.dao.DaoMethodConfig;
import com.ibm.db2.rca.service.integration.InputToService;

public class MetricDao
{

	/*public MetricDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) 
	{
		super(namedParameterJdbcTemplate);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Double> queryMaxPercentageContributionOfAllComponentsToTotalRequestTime_LevelDatabase(InputToService inputToService) throws Exception 
	{	
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		Following Local class definition is just put here to get the local function name within the function.
				
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
				
				BeanUtils.getProperty(inputToService, param);				
			}		
		}			
		
		return (Map<String, Double>) super.selectHashMap(daoMethodConfig, sqlParamValues);
	}*/
}
