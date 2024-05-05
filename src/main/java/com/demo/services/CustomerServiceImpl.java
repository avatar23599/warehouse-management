package com.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.Repository.CustomerRepository;
import com.demo.entities.Account;
import com.demo.entities.Customer;





@Service
public class CustomerServiceImpl  implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Iterable<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public boolean save(Customer customer) {
		try {
			customerRepository.save(customer);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Customer find(int id) {
		return customerRepository.findById(id).get();
	}

	@Override
	public boolean delete(int id) {
			try {
				customerRepository.delete(customerRepository.findById(id).get());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		
	}

	


}
