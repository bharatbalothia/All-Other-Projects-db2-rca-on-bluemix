package com.ibm.db2.rca.dao;

public class BaseDaoException extends Exception 
{
	private static final long serialVersionUID = -8522547449066777098L;

	public BaseDaoException() 
	{
		super();	
	}

	public BaseDaoException(String message, Throwable cause) 
	{
		super(message, cause);		
	}	

	public BaseDaoException(String message) 
	{
		super(message);
	}

	public BaseDaoException(Throwable cause) 
	{
		super(cause);	
	}

}
