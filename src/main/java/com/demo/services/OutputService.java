package com.demo.services;

import java.util.List;

import com.demo.entities.Output;


public interface OutputService {
	public  Output save(Output output);
	public List<Output> findAll2();
	
	public List<Output> findByStatus(String status);
	
	public Output find(int id);
}
