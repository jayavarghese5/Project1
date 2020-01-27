package com.csv.upload.product_data.controller;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	 
	public static String UPLOAD_DIRECTORY= System.getProperty("user.dir")+File.separator+"uploads"+File.separator;
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
		LocalDateTime localDateTime = LocalDateTime.now();
       //check if the file is chosen
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select the file to upload");
            LOGGER.error("File was not select , please select the file before submitting!!");
            return "uploadFile";
        }
        //get the extension of the file to further check if its csv file
        String filename = file.getOriginalFilename();
        if(! filename.contains("csv") ){
        	model.addAttribute("message", file.getOriginalFilename()+"  File is not CSV format ,Please select a csv file to upload");
        	 LOGGER.error("File was of different format ,Please select a CSV file!!");
        	return "uploadFile";
        }
        try {
        	// location to save the file for future reference
        	String pathValue=UPLOAD_DIRECTORY+localDateTime+filename;
        	//location of the  file , that contains error record that was not saved in the db
        	String errfilepathValue=UPLOAD_DIRECTORY+localDateTime+"ERROR"+filename;
        	File errfile=new File(errfilepathValue);
        	File filenew=new File(pathValue);
        	file.transferTo(filenew);
        	LOGGER.info("CSV file was saved for futher reference ");
        	String line,line1,linenew;
		    Scanner sc = new Scanner(filenew); 
		    String header=sc.nextLine();
		    while (sc.hasNextLine()) {
		    	//read the line and replace value with hash symbol so that we can get the count of total valid values
			    line=sc.nextLine().replaceAll("\",\"", "#");
			    // remove the remaining useless double quotes 
			    line=line.replace("\"", "");
			    // split the values with the hash symbol
			    String []values=line.split("#");
			    //if the no of values is less than the columns i.e 5 , then we will read the next line
			    //and check if the values is less than 5 , if its less then we would combine both to create the 
			    // incomplete line.
			    if(values.length<5) {
			    	line1=sc.nextLine().replaceAll("\",\"", "#");
			    	line1=line1.replace("\"", "");
			    	String []linevalues=line1.split("#");
			    	if(linevalues.length<5) {
				    	linenew=line+line1;
				    	String []values1=linenew.split("#");			    	 
				    	if(values1.length>5) {
				    		//our assumptions were wrong , so that values is saved in the errfile for further reference
				    		Files.write(Paths.get(errfilepathValue), line.getBytes());
				    		Files.write(Paths.get(errfilepathValue), linenew.getBytes());
				    	}
				    	else {
				    		values=values1;
				    	}
			    	}else
			    	if(linevalues.length==5) {
			    		// if the second line that we read is proper , then previous line need to be
			    		//entered in the errfile for futher refernce
			    		Files.write(Paths.get(errfilepathValue), line.getBytes());
			    		LOGGER.info("Error record found , saved in file!!");
			    		values=linevalues;
			    	}
			    	// if the values is greater than the columns then we need to save that input line in err file
			    	//for further reference
			    	if(values.length>5) {
			    		Files.write(Paths.get(errfilepathValue), line.getBytes());
			    		LOGGER.info("Error record found , saved in file!!");
			    	}
			    }
			    
                    outlet=new Outlet();
                    outlet.setId(Long.parseLong(values[0]));
                    outlet.setLastName(values[1]);
                    outlet.setLocation(values[2]);
                    outlet.setOutletName(values[3]);
                    outlet.setOutletType(values[4]);
                    System.out.println(outlet.toString());
                 // when we get the 5 column values , we are saving in the db
					uploadService.addOutlet(outlet);
                }
		       LOGGER.info("Upload successfull!!");
                model.addAttribute("success",
                        "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (Exception e) {
        	e.printStackTrace();
        	LOGGER.error("some error occured while uploading "+e.getStackTrace());
        	model.addAttribute("message",
                    "some exception occured while uploading file  '" + file.getOriginalFilename() + "' , please try again!! \n "+e.getMessage());
        }
        
        return "uploadFile";
    }
	
}
