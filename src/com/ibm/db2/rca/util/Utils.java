package com.ibm.db2.rca.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

public class Utils {


	
	public static boolean isNull(String value)
	{
		boolean isNull = false;
		
		if (value.compareToIgnoreCase("$null$") == 0)
		{
			isNull = true;
		}
		
		return isNull;
	}
	
	public static double stringToDouble(String value)
	{
		double doubleValue = 0.0;
		
		if (!isNull(value))
		{		
			doubleValue = Double.parseDouble(value);
		}
		
		return doubleValue;
	}
	
	public static long stringToLong(String value)
	{
		long doubleValue = 0L;
		
		if (!isNull(value))
		{		
			doubleValue = Long.parseLong(value);
		}
		
		return doubleValue;
	}
	
	public static boolean isEmpty(String value)
	{
		boolean truthness = false;
		
		if(value == null)
			truthness = true;
		else if(value.trim().length() == 0)
			truthness = true;
		else
			truthness = false;
		
		return truthness;
	}
	
	public static Timestamp parseDb2ExportedTimestamp(String timestampString)
	{
		
				
		//2014-12-02 18:48:08.150688
		
		Timestamp timestamp = null;
		
		  Pattern p = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}(\\.\\d{1,6})?$");
		  
		  Matcher m = p.matcher(timestampString);
		  
		  if(m.matches())
		  {
			  timestamp = Timestamp.valueOf(new String(timestampString));
		  }
		  else
		  {	      
				char [] timestampArray = timestampString.toCharArray();
				
				int dateElementSeparatorCount = 0;
				
				int timeElementSeparatorCount = 0; 
				
				for (int i=0; i<timestampArray.length; i++)
				{
					if (timestampArray[i] == '-')
			{
				dateElementSeparatorCount++;				
				
				if(dateElementSeparatorCount == 3)
				{
					timestampArray[i] = ' ';
				}
			}
			
			if (timestampArray[i] == '.')
			{
				timeElementSeparatorCount++;				
				
				if(timeElementSeparatorCount == 1 || timeElementSeparatorCount == 2)
				{
					timestampArray[i] = ':';
						}
					}
				}
				
				timestamp = Timestamp.valueOf(new String(timestampArray));
		  }
		
		return timestamp;
		
	}
	
	public static String buildSelectClause(List<String> columnNames)
	{
		String selectClause = null;
		
		if(columnNames.size() > 0)
		{
			StringBuilder sb = new StringBuilder(" ");
			
			for(String columnName : columnNames)
			{
				sb.append(columnName).append(", ");
			}
			
			selectClause = sb.toString();
			
			selectClause = selectClause.substring(0, selectClause.lastIndexOf(", ") - 1);
			
			selectClause = selectClause + " ";			
		}

		return selectClause;
		
	}
	
	
	public static void unzip(String zipFilenameWithPath, String outputFolderNameWithPath) throws IOException
	{
		int BUFFER  = 20480;
		
		BufferedInputStream bis = null;
		
		BufferedOutputStream bos = null;
		
		FileOutputStream fos = null;
		
		byte data[] = null;
		

		try
		{
	 
			//create output directory if not exists
			
			File extractIntoFolder = new File(outputFolderNameWithPath);
	    	
			if(!extractIntoFolder.exists())
	    	{
				extractIntoFolder.mkdir();
	    	}
			
			ZipFile zipFile = new ZipFile(zipFilenameWithPath);
			
			
			Enumeration zipFileEntries = zipFile.entries();
	 

	    	while(zipFileEntries.hasMoreElements())
	    	{
	    		
	    		ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
	    		
	    		String entryName = entry.getName();
	    		
	    		File destinationFile = new File(extractIntoFolder, entryName);
	    		
	    		File destinationParent = destinationFile.getParentFile();
	    		
	    		if(!destinationParent.exists())
		    	{
	    			destinationParent.mkdirs();
		    	}
	    		
	    		if (!entry.isDirectory())
	            {
	                bis = new BufferedInputStream(zipFile.getInputStream(entry));
	                
	                int currentByte;
	                // establish buffer for writing file
	                
	                data = new byte[BUFFER];

	                // write the current file to disk
	                fos = new FileOutputStream(destinationFile);
	                bos = new BufferedOutputStream(fos, BUFFER);

	                // read and write until last byte is encountered
	                while ((currentByte = bis.read(data, 0, BUFFER)) != -1) 
	                {
	                	bos.write(data, 0, currentByte);
	                }
	                

	            }
	    	}
	    	
	    	System.out.println("Done");
	 
	    }
	    catch(IOException ex)
	    {
	    	 ex.printStackTrace(); 
	    }
		finally
		{
            bos.flush();
            bos.close();
            bis.close();
		}
	     
	}   
	
    public static File createTempDirectory(String username) 
    {

        final File tmp = new File(FileUtils.getTempDirectory().getAbsolutePath() + File.separator + username + File.separator + System.currentTimeMillis());
        
        tmp.mkdir();
        
        Runtime.getRuntime().addShutdownHook(
        	new Thread() 
	        {	
	            @Override
	            public void run() {
	                try {
	                    FileUtils.deleteDirectory(tmp);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	       }
        );
        
        return tmp;
    }
}
