package com.csv.upload.product_data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csv.upload.product_data.model.Outlet;



@Repository
public interface OutletRepository<T> extends JpaRepository<Outlet,Long> { 

	public List<Outlet> findAll();
}
