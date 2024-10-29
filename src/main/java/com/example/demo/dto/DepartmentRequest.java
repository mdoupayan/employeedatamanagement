package com.example.demo.dto;

import lombok.Data;

@Data
public class DepartmentRequest {

	private String name;

	public DepartmentRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public DepartmentRequest() {
	}
}
