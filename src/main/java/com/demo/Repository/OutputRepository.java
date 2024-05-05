package com.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Category;
import com.demo.entities.Input;
import com.demo.entities.Inputifo;
import com.demo.entities.Output;
import com.demo.entities.Product;

@Repository
public interface OutputRepository extends CrudRepository<Output, Integer> {
	
	@Query("from Output where status = :status order by id Desc")
	public List<Output> findByStatus(@Param ("status") String status);
	
	@Query("from Output order by id Desc")
	public List<Output> findAll2();
	
}
