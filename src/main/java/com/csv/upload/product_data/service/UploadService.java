package com.csv.upload.product_data.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.csv.upload.product_data.model.Outlet;
import com.csv.upload.product_data.repository.OutletRepository;

@Component
public class UploadService {

	@Autowired
	OutletRepository<Outlet>  outletRepository;
	
	@Transactional(readOnly = true)
	public List<Outlet> getAllOutlets(){
		return (List<Outlet>) outletRepository.findAll();
	}
	
	@Transactional
	public Outlet addOutlet(Outlet outlet) {
		Outlet newOutlet=null;	
		//check if the outlet exist in db before adding it
		Optional<Outlet> duplicateOutlet=outletRepository.findById(outlet.getId());
		if(!duplicateOutlet.isPresent()) {
			//if duplicate data is not present then add the data
			newOutlet=outletRepository.save(outlet);	
		}
		return newOutlet;
	}
	
}
