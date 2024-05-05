package com.demo.services;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetailsService;


import com.demo.entities.Account;




public interface AccountService extends UserDetailsService{
	public Iterable<Account> findAll();
	
	public Account find(int id);
	
	public boolean login(String username, String password);
	
	public Account findByUsername(String username);
	 
	public boolean save(Account account);
	
	public boolean check(String username);
	
	public boolean delete(int id);
	
	public Account findByEmail(String email);
	
	public boolean editStatus(int id, String status);
	
	public List<Account> limit();
	
	public List<Account> limit12();
		
	public Integer getCurrentUserId();
	
	public List<Account> findByKeyword(String keyword);

	public String getHashedPassword(String username);
	
	public List<Account> searchByKeyword(String keyword);

	
}
