package com.example.demo.dto;

import lombok.Data;

@Data
public class EmployeeRequest {
	
	private String name;

	private String departmentName;

	private String password;

	private String role;

	private String email;

	public EmployeeRequest() {
	}

	public EmployeeRequest(String name, String email, String password, String role, String departmentName) {
		this.name = name;
		this.departmentName = departmentName;
		this.password = password;
		this.role = role;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
