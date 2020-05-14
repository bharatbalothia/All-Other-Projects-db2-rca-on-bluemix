package com.ibm.db2.rca.spring.mvc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.State;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.db2.rca.service.analyze.dblevel.HttpJsonInputToService;
import com.ibm.db2.rca.service.analyze.dblevel.InputToService;
import com.ibm.db2.rca.service.analyze.dblevel.OutputFromService;
import com.ibm.db2.rca.util.Utils;

@Controller
@RequestMapping(value = "/app/uploadMultipleFile.json")


public class FileUploadController 
{

	@Autowired
	private CustomerSession customerSession = null;

	/**
     * Upload multiple file using Spring Controller
	 * @throws IOException 
     */
    @RequestMapping(method = RequestMethod.POST, produces={"application/json"})
    public @ResponseBody
    List<FileUploadReturnJsonObject> uploadMultipleFileHandler(@RequestParam("uploadedfiles[]") MultipartFile[] files) throws IOException 
    {
 
    	System.err.println("I am inside the code ***********");
    	
    	BufferedOutputStream bos = null; 
    	
    	File uploadingFile = null;  
    	
    	List<FileUploadReturnJsonObject> fileList = new ArrayList<FileUploadReturnJsonObject>();
    	
    	FileUploadReturnJsonObject furjo = null;
        
        // Creating the directory to store file
        String rootPath = System.getProperty("java.io.tmpdir");
    	
    	//File tempFolderForUploadedFiles = Utils.createTempDirectory(this.customerSession.getCustomerID());

        File dir = new File(rootPath + File.separator + "tmpFiles");
        
        //if (!dir.exists())
            //dir.mkdirs();
        
        for (int i = 0; i < files.length; i++) 
        {
            MultipartFile file = files[i];
        
            try {
                
            	byte[] bytes = file.getBytes();
 

                // Create the file on server
            	uploadingFile = new File(dir.getAbsolutePath()+ File.separator + file.getOriginalFilename());
                bos = new BufferedOutputStream(new FileOutputStream(uploadingFile));
                bos.write(bytes);
                

                System.out.println("Filename : " + file.getOriginalFilename());
                
                furjo = new FileUploadReturnJsonObject();                
                furjo.setFile(file.getOriginalFilename());
                furjo.setName(file.getOriginalFilename());
                furjo.setType(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.')+1));
                furjo.setSize(bytes.length);
                
                fileList.add(furjo);
                
            } catch (Exception e) {
            	System.err.println("error************");                
            }
            finally
            {                
                bos.close();
            }
        }



    	
    	System.out.println("I am in side the code...");
    	
        return fileList;
    }
    
    
//    @RequestMapping(value="/app/service/fileToDbUploadStatus.do", method = RequestMethod.POST, produces={"application/json"})
//    public @ResponseBody OutputFromService dbLevelAnalysisService(@RequestBody HttpJsonInputToService httpJsonInputToService, UsernamePasswordAuthenticationToken authToken) throws IOException, ParseException 
//	{
//		OutputFromService outputFromService = null;
//		
//		try 
//		{
//			
//			this.dbLevelAnalyzer.setInputToService(inputToService);
//			
//			this.dbLevelAnalyzer.analyze();
//			
//			outputFromService = this.dbLevelAnalyzer.getOutputFromService();
//			
//		}
//		catch (Exception e) 
//		{		
//			System.out.println("Exception.");
//			e.printStackTrace();
//		}
//
//        return outputFromService;	
//    }


}