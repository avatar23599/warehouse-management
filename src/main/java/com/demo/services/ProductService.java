package com.demo.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.demo.entities.Account;
import com.demo.entities.Product;
import com.demo.entities.Suplier;

public interface ProductService {
	public Iterable<Product> findAll();

	public boolean save(Product account);
	
	public boolean delete(int id);
	
	public Product find(int id);
	
	public List<Product> findSuplier(int suplier_id);
	
	public List<Product> searchByKeyword(String keyword);
	
	
	
	

}
