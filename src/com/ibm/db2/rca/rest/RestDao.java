package com.ibm.db2.rca.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ibm.db2.rca.dao.BaseDaoImpl;
import com.ibm.db2.rca.dao.DaoMethodConfig;
import com.ibm.db2.rca.service.integration.InputToService;

public class RestDao extends BaseDaoImpl {

	//@Autowired 
	private InputToService inputToService;
	
	public RestDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) 
	{
		super(namedParameterJdbcTemplate);
	}
	
	public List<Map<String, String>> queryDatabaseListForTheLoggedInCustomer_LevelConfig() throws Exception
	{
		Map<String,Object> sqlParamValues = new HashMap<String,Object>();

		/*Following Local class definition is just put here to get the local function name within the function.*/
		System.out.println("queryDatabaseListForTheLoggedInCustomer_LevelConfig DAO method called.");
		
		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) this.getDaoMethods().get(methodName);
		
		List<String> params = daoMethodConfig.getSqlParams();
		
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
		
		System.out.println(this.getClass().getSimpleName() + " : Object ID : " + this.hashCode());
		
		return (List<Map<String, String>>) super.selectList(daoMethodConfig, sqlParamValues,null);
	}

}
