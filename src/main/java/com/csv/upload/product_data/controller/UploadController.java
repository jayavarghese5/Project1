package com.csv.upload.product_data.controller;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.csv.upload.product_data.model.Outlet;
import com.csv.upload.product_data.service.UploadService;

@Controller
public class UploadController {
	private static final Logger LOGGER=LoggerFactory.getLogger(UploadController.class);
	LocalDateTime localDateTime = LocalDateTime.now(); 
	public static String UPLOAD_DIRECTORY= System.getProperty("user.dir")+File.separator+"uploads"+File.separator;
	String pathValue="",errfilepathValue;
	File errfile;
	@Autowired 
	UploadService uploadService;
	@GetMapping("/")
    public String index(Model model) {
        return "uploadFile";
    }
	
	/**
	 * This method will save the file for future reference in the uploads folder , and put the values of the csv
	 * file in the database.
	 * @param file
	 * @param model
	 * @return
	 */
	@PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   Model model) {
    	Outlet outlet;
		
       
        String value=validateFile(file,model);
        if(value.equalsIgnoreCase("valid")) {
        	File filenew=saveFileForFutureReference(file);
        	try {

            	String line,line1 = "",linenew;
    		    Scanner sc = new Scanner(filenew); 
    		    //skip the heading of the csv file.
    		    String header=sc.nextLine();
    		    while (sc.hasNextLine()) {
    		    	String []values1 = null;
    		    	line=sc.nextLine();
    		    	String []values=splitLinesTocolums(line);
 	   //condition 1: first line was less than 5 but next line had the remaining values, then read next line and check if its addition
 //is greater than 5 , if the values is less then add line 1 and line 2 and check if the total values are 
  //equal to 5. if its equal then our assumption was correct , if its not equal then our assumption was wrong 1st 
//line was a err line so that value is entered in the err file.
    		    	if(values.length<5) {
    		    	   line1=sc.nextLine();
    		    	   values1=splitLinesTocolums(line1);
    		    	   if(values1.length<5) {
       		    		linenew=line+line1;
       		    		System.out.println("linenew****"+linenew);
       		    		String []values2=splitLinesTocolums(linenew);
       		    		saveValid(values2,line);
       		    	   }
    		    	}
    		    	if(values.length>5) {
    		    		saveValid(values,line);	
    		    	}
    		    	String []values3=splitLinesTocolums(line1);
    		    	saveValid(values3,line1);
    		    	saveValid(values,line);
    		    }
    		    LOGGER.info("Upload successfull!!");
                model.addAttribute("success",
                        "You successfully uploaded '" + file.getOriginalFilename() + "'");
    		    sc.close();
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        		LOGGER.error("some error occured while uploading "+e.getStackTrace());
            	model.addAttribute("message",
                        "some exception occured while uploading file  '" + file.getOriginalFilename() 
                        + "' , please try again!! \n "+e.getMessage());           	
        	}
        }
        else 
         return "uploadFile";
        
		return "uploadFile";
    }
	private boolean saveValid(String[] values2, String line){
		boolean saved=false;
		if(values2.length>5 || values2.length<5) {
    		//our assumptions were wrong , so this values is saved in the errfile for further reference
			try {
			appendStringToFile(errfile,line);			
				//Files.write(Paths.get(errfilepathValue), line.getBytes());
    		} catch (IOException e) {
				LOGGER.error("Fail to write in file "+e.getMessage());
				e.printStackTrace();
			}
    	}
   		//check if second line length is 5 and save in db
   		if(values2.length==5) {
   			saveToDb(values2);
   			saved=true;
    	}
		return true;
	}

	private Outlet saveToDb(String []values) {
		System.out.println("****"+values.length);
		Outlet outlet=new Outlet();
        outlet.setId(Long.parseLong(values[0]));
        outlet.setLastName(values[1]);
        outlet.setLocation(values[2]);
        outlet.setOutletName(values[3]);
        outlet.setOutletType(values[4]);
        System.out.println(outlet.toString());
        outlet=uploadService.addOutlet(outlet);
		return outlet;
	}

	private String[] splitLinesTocolums(String lineVal) {
		String line;
    	//read the line and replace value with hash symbol so that we can get the count of total valid values
	    line=lineVal.replaceAll("\",\"", "#");
	    // remove the remaining useless double quotes 
	    line=line.replace("\"", "");
	    // split the values with the hash symbol
	    String []values=line.split("#");
		return values;
	}

	private File saveFileForFutureReference(MultipartFile file) {
    	// location to save the file for future reference
    	String pathValue=UPLOAD_DIRECTORY+localDateTime+file.getOriginalFilename();
    	//location of the  file , that contains error record that was not saved in the db 
    	errfilepathValue=UPLOAD_DIRECTORY+localDateTime+"ERROR"+file.getOriginalFilename();
    	errfile=new File(errfilepathValue);
    	file.getOriginalFilename();
    	File filenew=new File(pathValue);
    	try {
			file.transferTo(filenew);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	LOGGER.info("CSV file was saved for futher reference ");
		return filenew;
	}

	public String validateFile(MultipartFile file,Model model) {
        String filename = file.getOriginalFilename();
		//check if the file is chosen
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select the file to upload");
            LOGGER.error("File was not select , please select the file before submitting!!");
            return "uploadFile";
        }
        //get the extension of the file to further check if its csv file

        if(! filename.contains("csv") ){
        	model.addAttribute("message", file.getOriginalFilename()+"  File is not CSV format ,Please select a csv file to upload");
        	 LOGGER.error("File was of different format ,Please select a CSV file!!");
        	return "uploadFile";
        }
		return "valid";
		
	}
	
	static void appendStringToFile(File file, String s) throws IOException  {
		if (!file.exists()) {
			 try {
				 file.createNewFile();
			} catch (IOException e) {
				LOGGER.error("could not create the file "+e.getMessage());
			}
        }
		FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
        BufferedWriter bw = new BufferedWriter(fw);
        // Write in file
        bw.write(s);
        // Close connection
        bw.close();
	}
}

