package com.demo.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.demo.entities.Account;
import com.demo.entities.Category;



public interface CategoryService {
	public Iterable<Category> findAll();
	public boolean save(Category account);
	
	public boolean delete(int id);
	
	public Category find(int id);
	
	public List<Category> searchByKeyword(String keyword);

}
