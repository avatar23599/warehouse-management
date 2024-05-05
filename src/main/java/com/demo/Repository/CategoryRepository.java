package com.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Account;
import com.demo.entities.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
	@Query("from Category where name like %:keyword%")
	public List<Category> searchByKeyword(@Param("keyword") String keyword);
}
