package com.demo.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.Repository.InputRepository;
import com.demo.dtos.InputDTO;
import com.demo.entities.Category;
import com.demo.entities.Input;
import com.demo.entities.Inputifo;


@Service
public class InputServiceImpl implements InputService {
	
	@Autowired
	private InputRepository inputRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Iterable<Input> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Input save(InputDTO inputDTO) {
		try {
			Input input = modelMapper.map(inputDTO, Input.class);
			inputRepository.save(input);
			return inputRepository.save(input);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Input find(int id) {
		return inputRepository.findById(id).get();
	}

	@Override
	public List<Input> findByStatus(String status) {
		try {
			return inputRepository.findByStatus(status);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean save(Input input) {
		try {
			inputRepository.save(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<Input> findAll2() {
		return inputRepository.findAll2();
	}

	

}
