package com.ibm.db2.rca.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.ibm.db2.rca.service.analyze.dblevel.InputToService;

public class JsonCloner 
{

	public static <T> T getObjectFromJsonData( byte[] jsonData, Class <T> clazz) throws JsonParseException, JsonMappingException, IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		
		T nt = mapper.readValue(jsonData, clazz);
		
		return nt;
	}
	
	public static <T> byte[] getJsonDataFromObject(T object) throws JsonGenerationException, JsonMappingException, IOException
	{
		byte[] jsonData;
		
		ObjectMapper mapper = new ObjectMapper();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		mapper.writeValue(bos, object);
		
		jsonData = bos.toByteArray();
		
		bos.close();
		
		return jsonData;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T clone(T t) throws JsonGenerationException, JsonMappingException, IOException
	{
		byte[] jsonData;
		
		T clonnedObject;
		
		jsonData = JsonCloner.getJsonDataFromObject(t);
		
		clonnedObject = (T) JsonCloner.getObjectFromJsonData(jsonData, t.getClass());
		
		return clonnedObject;
	}
	
	public static void main (String [] args) throws JsonGenerationException, JsonMappingException, IOException
	{
		
	
		
		Test test = new Test();
		
		test.getList().add("String 1");
		test.getList().add("String 3");
		test.getList().add("String 2");
		
		test.setX(1);
		
		
		
		System.out.println("Before clonning : Test.X : " + test.getX() + ", ArrayList Size : " + test.getList().size());
		
		Test clonnedTest = JsonCloner.clone(test);

		test.getList().add("String 5");
		test.getList().add("String 6");
		test.getList().add("String 7");
		
		test.setX(102);
		
		System.out.println("After clonning : Test.X : " + test.getX() + ", ArrayList Size : " + test.getList().size());
		
		System.out.println("After clonning Clonned Object : clonnedTestTest.X : " + clonnedTest.getX() + ", ArrayList Size : " + clonnedTest.getList().size());

		
		
		
		
	}
	
}
