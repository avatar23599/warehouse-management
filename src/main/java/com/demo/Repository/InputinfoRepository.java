package com.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Category;
import com.demo.entities.Input;
import com.demo.entities.Inputifo;

@Repository
public interface InputinfoRepository extends CrudRepository<Inputifo, Integer> {
	@Query("from Inputifo where input.id = :id")
	public List<Inputifo> findByIdInput(@Param ("id") int id);
}
