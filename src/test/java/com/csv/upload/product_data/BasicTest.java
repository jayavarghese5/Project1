package com.csv.upload.product_data;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.csv.upload.product_data.model.Outlet;


public class BasicTest {
	
	public static void main(String args[]) throws IOException {
	/* String filename="abc.csv";
		LocalDateTime localDateTime = LocalDateTime.now();
		String UPLOAD_DIRECTORY= System.getProperty("user.dir")+File.separator+"uploads"+File.separator;
		 String pathValue=UPLOAD_DIRECTORY+localDateTime+filename;
		 System.out.println(pathValue);*/
		 
		    System.out.println("********");
		    String line,line1,linenew;
		    Scanner sc = new Scanner(new File("/Users/macbookpro/Documents/test.csv")); 
		    String header=sc.nextLine();
		    while (sc.hasNextLine()) {
			    line=sc.nextLine().replaceAll("\",\"", "#");
			    line=line.replace("\"", "");
			    String []values=line.split("#");
			    if(values.length<5) {
			    	line1=sc.nextLine().replaceAll("\",\"", "#");
			    	line1=line1.replace("\"", "");
			    	String []linevalues=line1.split("#");
			    	if(linevalues.length<5) {
				    	linenew=line+line1;
				    	String []values1=linenew.split("#");			    	 
				    	if(values1.length>5) {
				    		System.out.println("some error while reading");
				    	}
				    	else {
				    		values=values1;
				    	}
			    	}else
			    	if(linevalues.length==5) {
			    		values=linevalues;
			    	}
			    }
			    
	     	    System.out.println("********"+Arrays.toString(values)+" length"+values.length);
			    

		    }
		/* FileInputStream inputStream = null;
		 Scanner sc = null;
		 try {
		     inputStream = new FileInputStream("/Users/macbookpro/Documents/data.csv");
		     sc = new Scanner(inputStream, "UTF-8");
		     while (sc.hasNextLine()) {
		         String line = sc.nextLine();
		         System.out.println(line);
		     }
		     if (sc.ioException() != null) {
		         throw sc.ioException();
		     }
		 } finally {
		     if (inputStream != null) {
		         inputStream.close();
		     }
		     if (sc != null) {
		         sc.close();
		     }
		 }*/
		 
		  
	}
	
	
}