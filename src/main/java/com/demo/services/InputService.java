package com.demo.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.demo.dtos.InputDTO;
import com.demo.entities.Category;
import com.demo.entities.Input;
import com.demo.entities.Inputifo;



public interface InputService {
	
	public Iterable<Input> findAll();
	
	public Input save(InputDTO inputDTO);
	
	public boolean save(Input Input);
	
	public boolean delete(int id);
	
	public Input find(int id);
	
	public List<Input> findByStatus(String status);
	
	public List<Input> findAll2();

}
