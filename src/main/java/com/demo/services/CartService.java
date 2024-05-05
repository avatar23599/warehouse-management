package com.demo.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.demo.entities.Item;

public interface CartService {
	public double total(List<Item> items);
	
	public int exist(int productId, List<Item> items);
	
	public List<List<String>> uploadExcel(MultipartFile excelFile);
}
