package com.ibm.db2.rca.service.analyze.component.bufferpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ibm.db2.rca.dao.BaseDaoImpl;
import com.ibm.db2.rca.dao.DaoMethodConfig;
import com.ibm.db2.rca.spring.mvc.CustomerSession;



public class BufferPoolDao extends BaseDaoImpl 
{
	@Autowired
	private CustomerSession customerSession = null;
	
	public BufferPoolDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) 
	{
		super(namedParameterJdbcTemplate);
	}

		
	public double getMaxReadResponseTime() throws Exception 
	{
	
		double maxReadResponseTime = 0.0;

		

		/*Following Local class definition is just put there to get the local function name within the function.*/
		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		DaoMethodConfig daoMethodConfig = (DaoMethodConfig) super.getDaoMethods().get(methodName);
		
		super.selectObject(daoMethodConfig, null,null);
		
		

		return maxReadResponseTime;
	}

}
