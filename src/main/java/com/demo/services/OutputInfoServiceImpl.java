package com.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.Repository.OutputRepository;
import com.demo.Repository.OutputinfoRepository;
import com.demo.entities.Inputifo;
import com.demo.entities.Outputinfor;

@Service
public class OutputInfoServiceImpl implements OutputInfoService{

	@Autowired
	private OutputinfoRepository outputInfoRepository;
	
	@Override
	public boolean save(Outputinfor outputinfo) {
		try {
			outputInfoRepository.save(outputinfo);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	@Override
	public List<Outputinfor> findByIdOutput(int id) {
		return outputInfoRepository.findByIdOutput(id);
	}

}
