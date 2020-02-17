package com.csv.upload.product_data;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import com.csv.upload.product_data.controller.UploadController;
import com.csv.upload.product_data.model.Outlet;
import com.csv.upload.product_data.repository.OutletRepository;
import com.csv.upload.product_data.service.UploadService;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = UploadController.class)
class ProductDataApplicationTests {
	
	  @Autowired
	  private WebApplicationContext wac;
	  private MockMvc mockMvc;
	  
	  @Mock
	  @Autowired
	   private OutletRepository<Outlet> outletRepository;
	  
	  @InjectMocks // auto inject outletRepository
	    private UploadService uploadService = new UploadService();
	  
	  @Autowired
      UploadController uploadController=new UploadController();
	  
	  Model model=new ExtendedModelMap();


	
	  @Test
	  public void testUploadFileWithWrongFormat() throws Exception {
	      String fileName = "test1.doc";
	      File file = new File(UploadController.UPLOAD_DIRECTORY + fileName);
	      MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file",fileName,
	              "text/plain", "test data".getBytes());
          try {
	      uploadController.singleFileUpload(mockMultipartFile, model);
	      boolean value=model.asMap().get("message").toString().contains("File is not CSV format ,Please select a csv file to upload");
	      org.junit.jupiter.api.Assertions.assertTrue(value);
          }
          catch(Exception e) {
        	  System.out.println( e.getMessage());
        	  fail("Some err occured");
          }  
	  }

	  @Test
	  public void testUploadFileWithEmptyorNull() throws Exception {
		    byte[] content = new byte[0];
		    MockMultipartFile multiPartMock = new MockMultipartFile("csvFile", "mock.csv", null, content);
          try {
	      uploadController.singleFileUpload(multiPartMock, model);
	      boolean value=model.asMap().get("message").toString().contains("Please select the file to upload");
	      org.junit.jupiter.api.Assertions.assertTrue(value);
          }
          catch(Exception e) {
        	  System.out.println("Err"+ e.getMessage());
        	  fail("Some error occured");
          }  
	  }

	 // @Test
	  public void testUploadCSVWithValidandInvalidValues() throws Exception {
	      String fileName = "test.csv";
	      FileInputStream fis = new FileInputStream(UploadController.UPLOAD_DIRECTORY + fileName);
	    //  MockMultipartFile multipartFile = new MockMultipartFile("test.csv", fis);
	      MockMultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "text/plain", fis);
	      Outlet outlet = new Outlet();
		  outlet.setId(2L);
		  outlet.setLastName("MC donald");
          try { 
		  UploadService uploadService = mock(UploadService.class);
	      when(uploadService.addOutlet(outlet)).thenReturn(outlet);

	      uploadController.singleFileUpload(multipartFile, model);
	      System.out.println("***"+model.asMap().get("success").toString());
	      boolean value=model.asMap().get("success").toString().contains("You successfully uploaded");
	      org.junit.jupiter.api.Assertions.assertTrue(value);
          }
          catch(Exception e) {
        	  e.printStackTrace();
        	  System.out.println("Err"+ e.getMessage());
        	  fail("Some error occured");
          }  
	  }
	  
	  //@Test
	  public void testUploadCSVWithValidValues() throws Exception {
	      String fileName = "data.csv";
	      FileInputStream fis = new FileInputStream(UploadController.UPLOAD_DIRECTORY + fileName);
	      MockMultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "text/plain", fis);
	      Outlet outlet = new Outlet();
		  outlet.setId(2L);
		  outlet.setLastName("MC donald");
          try { 
		  UploadService uploadService = mock(UploadService.class);
	      when(uploadService.addOutlet(outlet)).thenReturn(outlet);

	      uploadController.singleFileUpload(multipartFile, model);
	      System.out.println("***"+model.asMap().get("success").toString());
	      boolean value=model.asMap().get("success").toString().contains("You successfully uploaded");
	      org.junit.jupiter.api.Assertions.assertTrue(value);
          }
          catch(Exception e) {
        	  e.printStackTrace();
        	  System.out.println("Err"+ e.getMessage());
        	  fail("failed Exception occured");
          }  
	  }
	  
	  //@Test
	  public void testUploadCSVWithValuesGreaterthanColumn() throws Exception {
	      String fileName = "test1.csv";
	      FileInputStream fis = new FileInputStream(UploadController.UPLOAD_DIRECTORY + fileName);
	      MockMultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "text/plain", fis);
	      Outlet outlet = new Outlet();
		  outlet.setId(2L);
		  outlet.setLastName("MC donald");
          try { 
		  UploadService uploadService = mock(UploadService.class);
	      when(uploadService.addOutlet(outlet)).thenReturn(outlet);

	      uploadController.singleFileUpload(multipartFile, model);
	      System.out.println("***"+model.asMap().get("success").toString());
	      boolean value=model.asMap().get("success").toString().contains("You successfully uploaded");
	      org.junit.jupiter.api.Assertions.assertTrue(value);
          }
          catch(Exception e) {
        	  e.printStackTrace();
        	  System.out.println("Err"+ e.getMessage());
        	  fail("Some error occured");
          }  
	  }
	  
	  @AfterAll
	  public static void clearTestCSVFiles() {
		  File dir = new File(UploadController.UPLOAD_DIRECTORY);
	        File[] files = dir.listFiles();
	        if (files != null && files.length > 0) {
	            for (File file : files) {
	            	if(file.getName().matches("\\d{4}-\\d{2}-\\d{2}.*.csv")){
	            		file.delete();	
	            	}                          
	            }
	        }
	  }
}
