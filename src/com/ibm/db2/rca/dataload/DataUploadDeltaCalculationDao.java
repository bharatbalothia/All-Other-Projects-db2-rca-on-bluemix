package com.ibm.db2.rca.dataload;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ibm.db2.rca.dao.BaseDaoImpl;
import com.ibm.db2.rca.dao.DaoMethodConfig;
import com.ibm.db2.rca.spring.mvc.CustomerSession;
import com.ibm.db2.rca.util.JsonCloner;
import com.ibm.db2.rca.util.Utils;


public class DataUploadDeltaCalculationDao extends BaseDaoImpl
{
	@Autowired
	private CustomerSession customerSession = null;
	
	
	@Resource(name="dbLevelDataLoadDescriptors")
	private Map<String,Map<String,String>> dbLevelDataLoadDescriptors = null;
	
	public DataUploadDeltaCalculationDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) 
	{
		super(namedParameterJdbcTemplate);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryMonitoredData_LevelDatabase(List<String> listOfOutputSqlColumns) throws Exception 
	{	

		/*Following Local class definition is just put here to get the local function name within the function.*/

		class Local{};
		
		String methodName = Local.class.getEnclosingMethod().getName();
		
		System.out.println("Method Name : " + methodName);
		
		DaoMethodConfig clonnedDaoMethodConfig = JsonCloner.clone((DaoMethodConfig) this.getDaoMethods().get(methodName));
		
		String selectClausePhrase = null;
		
		selectClausePhrase = Utils.buildSelectClause(listOfOutputSqlColumns);

		String sql = clonnedDaoMethodConfig.getSql();
		
		sql = sql.replace(super.dynamicSelectPhraseFragmentFromMethod, selectClausePhrase);
		
		clonnedDaoMethodConfig.setSql(sql);
		
		clonnedDaoMethodConfig.setListOfOutputSqlColumns(listOfOutputSqlColumns);
		
		return (List<Map<String, Object>>) super.selectList(clonnedDaoMethodConfig, null, this.customerSession.getCustomerID());
	}
	
	 
}
