package com.demo.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.demo.entities.Account;
import com.demo.entities.Product;
import com.demo.entities.Suplier;

public interface SuplierService {
	public Iterable<Suplier> findAll();

	public boolean save(Suplier account);
	
	public boolean delete(int id);
	
	public Suplier find(int id);
	
	public List<Suplier> searchByKeyword(String keyword);
	
	

}
