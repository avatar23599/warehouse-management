package com.demo.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Account;
import com.demo.entities.Suplier;

@Repository
public interface SuplierRepository extends CrudRepository<Suplier, Integer> {

	@Query("from Suplier where name like %:keyword%")
	public List<Suplier> searchByKeyword(@Param("keyword") String keyword);
	
}
