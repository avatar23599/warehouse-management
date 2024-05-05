package com.demo.dtos;

import java.time.LocalDateTime;
import java.util.Date;

public class InputDTO {
	private int id;
	
	private LocalDateTime date;
	
	private int accountId;
	
	private int suplierId;
	
	private String inputinfo;
	
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime  date) {
		this.date = date;
	}


	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getSuplierId() {
		return suplierId;
	}

	public void setSuplierId(int suplierId) {
		this.suplierId = suplierId;
	}


	public String getInputinfo() {
		return inputinfo;
	}

	public void setInputinfo(String inputinfo) {
		this.inputinfo = inputinfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
