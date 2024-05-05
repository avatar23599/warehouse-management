package com.demo.Repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.entities.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	
	
}
