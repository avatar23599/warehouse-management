package com.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.Repository.InputRepository;
import com.demo.Repository.OutputRepository;
import com.demo.entities.Output;

@Service
public class OutputServiceImpl implements OutputService{

	@Autowired
	private OutputRepository outputRepository;
	
	@Override
	public Output save(Output output) {
		try {
			return outputRepository.save(output);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Output> findAll2() {
		return outputRepository.findAll2();
	}

	@Override
	public List<Output> findByStatus(String status) {
		return outputRepository.findByStatus(status);
	}

	@Override
	public Output find(int id) {
		return outputRepository.findById(id).get();
	}

}
