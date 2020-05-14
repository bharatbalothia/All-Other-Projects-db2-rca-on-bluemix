package com.ibm.db2.rca.dao;


import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface BaseDao 
{
	public enum Cardinality {RnCn, R1C1, R1Cn, RnC1};

	public List<?> selectRnCnObjects(Map<String, Object> params)
			throws DataAccessException, ClassNotFoundException;


	public List<?> selectRnCn(Map<String, Object> params) throws DataAccessException,
			ClassNotFoundException;


	public Object selectR1C1Object(Map<String, Object> params)
			throws DataAccessException, ClassNotFoundException;


	double selectR1C1Double(Map<String, Object> params)
			throws DataAccessException, ClassNotFoundException;

}
