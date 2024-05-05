package com.demo.services;


import java.util.List;

import com.demo.entities.Account;
import com.demo.entities.Product;
import com.demo.entities.Role;


public interface RoleService {
	
	public Iterable<Role> findAll();
	
	public Role find(int id);
	
	public List<Role> limit1(int n);

	public List<Role> limit();
	
	public List<Role> limit12();
	
	public List<Role> limit13();
	
	public List<Role> limit23();
	
	public List<Role> limit2(int start, int n);
	
}
