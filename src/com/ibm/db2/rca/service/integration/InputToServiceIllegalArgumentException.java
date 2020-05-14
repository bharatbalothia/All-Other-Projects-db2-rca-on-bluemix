package com.ibm.db2.rca.service.integration;

public class InputToServiceIllegalArgumentException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1912517850517237545L;
	
    public InputToServiceIllegalArgumentException() 
    {
        super();
    }
    
    public InputToServiceIllegalArgumentException(String message) 
    {
        super(message);
    }

    public InputToServiceIllegalArgumentException(String message, Throwable throwable) 
    {
        super(message, throwable);        
    }
    
    public InputToServiceIllegalArgumentException(Throwable cause) 
    {
        super(cause);
    }
	
}
