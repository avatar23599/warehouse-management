package com.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.Repository.SuplierRepository;
import com.demo.entities.Account;
import com.demo.entities.Suplier;



@Service
public class SuplierServiceImpl  implements SuplierService{

	@Autowired
	private SuplierRepository suplierRepository;

	@Override
	public Iterable<Suplier> findAll() {
		return suplierRepository.findAll();
	}

	@Override
	public boolean save(Suplier suplier) {
		try {
			suplierRepository.save(suplier);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Suplier find(int id) {
		return suplierRepository.findById(id).get();
	}

	@Override
	public boolean delete(int id) {
			try {
				suplierRepository.delete(suplierRepository.findById(id).get());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		
	}

	@Override
	public List<Suplier> searchByKeyword(String keyword) {
		return suplierRepository.searchByKeyword(keyword);
	}


}
