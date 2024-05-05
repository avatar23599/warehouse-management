package com.demo.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.Repository.AccountRepository;

import com.demo.Repository.RoleRepository;
import com.demo.entities.Account;
import com.demo.entities.Role;

@Service
public class RoleServiceImpl implements RoleService{
	
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public Role find(int id) {
		return roleRepository.findById(id).get();
	}

	@Override
	public Iterable<Role> findAll() {
		return roleRepository.findAll();
	}

	@Override
	public List<Role> limit() {
		return roleRepository.limit();
	}

	@Override
	public List<Role> limit1(int n) {
		return roleRepository.limit1(n);
	}

	@Override
	public List<Role> limit12() {
		return roleRepository.limit12();
	}

	@Override
	public List<Role> limit2(int start, int n) {
		return roleRepository.limit2(start, n);
	}

	@Override
	public List<Role> limit13() {
		return roleRepository.limit13();
	}

	@Override
	public List<Role> limit23() {
		return roleRepository.limit23();
	}

	

}
