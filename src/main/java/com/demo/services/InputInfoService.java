package com.demo.services;

import java.util.List;

import com.demo.dtos.InputDTO;
import com.demo.dtos.InputInfoDTO;
import com.demo.entities.Inputifo;

public interface InputInfoService {
	
	public boolean save(Inputifo inputInfo);
	
	public List<InputInfoDTO> findByIdInput(int id);
	
	public InputInfoDTO findById(int id);
	
	public boolean save(InputInfoDTO InputinfoDTO);

}
