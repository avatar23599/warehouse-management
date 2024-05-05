package com.demo.Repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Account;
import com.demo.entities.Category;
import com.demo.entities.Product;





@Repository
public interface AccountRepository extends CrudRepository<Account, Integer>{
		
	@Query("from Account where username = :username and password = :password")
	public boolean login(@Param("username") String username, 
		@Param("password") String password);
	
	@Query("from Account where username = :username")
	public Account findByUsername(@Param ("username")String username);
	
	@Query("from Account where email = :email")
	public Account findByEmail(@Param("email") String email);
	
	@Query(" from Account where role.id != 1")
	public List<Account> limit();
	
	@Query(" from Account where role.id != 1 and role.id != 2")
	public List<Account> limit12();
	
	@Query("from Account where username like %:keyword")
	public List<Account> findByKeyword(@Param("keyword") String keyword);
	
	@Query("from Account where username like %:keyword%")
	public List<Account> searchByKeyword(@Param("keyword") String keyword);
	
	
	
	

	
	
}
