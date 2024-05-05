package com.demo.dtos;

import java.time.LocalDateTime;
import java.util.Date;

import com.demo.entities.Input;
import com.demo.entities.Product;

public class InputInfoDTO {
	private Integer id;
	private Input input;
	private Product product;
	private int count;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Input getInput() {
		return input;
	}
	public void setInput(Input input) {
		this.input = input;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	

}
