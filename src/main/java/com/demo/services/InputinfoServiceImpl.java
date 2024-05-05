package com.demo.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.Repository.InputinfoRepository;
import com.demo.dtos.InputDTO;
import com.demo.dtos.InputInfoDTO;
import com.demo.entities.Inputifo;


@Service
public class InputinfoServiceImpl implements InputInfoService{
	
	@Autowired
	private InputinfoRepository inputinfoRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public boolean save(Inputifo inputinfo) {
		try {
			inputinfoRepository.save(inputinfo);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	@Override
	public List<InputInfoDTO> findByIdInput(int id) {
		return modelMapper.map(inputinfoRepository.findByIdInput(id), 
				new TypeToken<List<InputInfoDTO>> () {}.getType()) ;
	}

	@Override
	public InputInfoDTO findById(int id) {
		return modelMapper.map(inputinfoRepository.findById(id).get(), InputInfoDTO.class);
	}

	@Override
	public boolean save(InputInfoDTO inputinfoDTO) {
		try {
			Inputifo inputinfo = modelMapper.map(inputinfoDTO, Inputifo.class);
			inputinfoRepository.save(inputinfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
