package com.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.entities.Category;
import com.demo.entities.Input;
import com.demo.entities.Inputifo;
import com.demo.entities.Outputinfor;

@Repository
public interface OutputinfoRepository extends CrudRepository<Outputinfor, Integer> {
	@Query("from Outputinfor where output.id = :id")
	public List<Outputinfor> findByIdOutput(@Param ("id") int id);
}
