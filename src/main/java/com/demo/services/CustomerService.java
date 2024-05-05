package com.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.demo.entities.Customer;


public interface CustomerService {
	public Iterable<Customer> findAll();

	public boolean save(Customer account);
	
	public boolean delete(int id);
	
	public Customer find(int id);
	
	

}
