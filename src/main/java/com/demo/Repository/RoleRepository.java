package com.demo.Repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Account;
import com.demo.entities.Product;
import com.demo.entities.Role;





@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{
	@Query(" from Role where id != 1")
	public List<Role> limit();
	
	@Query(value = "select * from Role order by id desc limit :n", nativeQuery = true)
	public List<Role> limit1(@Param("n") int n );

	@Query(" from Role where id != 1 and id !=2" )
	public List<Role> limit12();
	
	@Query(" from Role where id != 1 and id !=3" )
	public List<Role> limit13();
	
	@Query(" from Role where id != 2 and id != 3")
	public List<Role> limit23();
	
	@Query(value = "select * from Role order by id desc limit :start, :n", nativeQuery = true)
	public List<Role> limit2(@Param("start") int start,  @Param("n") int n);
	
}
