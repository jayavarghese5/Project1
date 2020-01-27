package com.csv.upload.product_data;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.csv.upload.product_data.controller.UploadController;


@SpringBootApplication
@ComponentScan(basePackages = "com.csv.upload.product_data.*")
public class ProductDataApplication {

	public static void main(String[] args) {
		new File(UploadController.UPLOAD_DIRECTORY).mkdir();
		SpringApplication.run(ProductDataApplication.class, args);
	}

}
