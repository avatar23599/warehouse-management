package com.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.Repository.ProductRepository;

import com.demo.entities.Product;



@Service
public class ProductServiceImpl  implements ProductService{

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Iterable<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public boolean save(Product product) {
		try {
			productRepository.save(product);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Product find(int id) {
		return productRepository.findById(id).get();
	}

	@Override
	public List<Product> findSuplier(int suplier_id) {
		return productRepository.findByIdSuplier(suplier_id);
	}

	@Override
	public boolean delete(int id) {
			try {
				productRepository.delete(productRepository.findById(id).get());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		
	}

	@Override
	public List<Product> searchByKeyword(String keyword) {
		return productRepository.searchByKeyword(keyword);
	}


}
