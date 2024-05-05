package com.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Account;
import com.demo.entities.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

	@Query("from Product where suplier.id = :suplier_id")
	public List<Product> findByIdSuplier(@Param ("suplier_id") int suplier_id);
	
	@Query("from Product where name like %:keyword%")
	public List<Product> searchByKeyword(@Param("keyword") String keyword);
	
}
