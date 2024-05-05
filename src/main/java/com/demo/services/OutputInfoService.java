package com.demo.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.demo.dtos.InputDTO;
import com.demo.entities.Category;
import com.demo.entities.Input;
import com.demo.entities.Inputifo;
import com.demo.entities.Output;
import com.demo.entities.Outputinfor;



public interface OutputInfoService {
	
	
	
	public boolean save(Outputinfor outputInfo);
	
	public List<Outputinfor> findByIdOutput(int id);
	
	

}
