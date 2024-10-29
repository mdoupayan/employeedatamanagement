package com.example.demo.dto;

import lombok.Data;

@Data
public class EmployeeDTO {
    private int id;
    private String name;
    private String email;
    private String role;
    private String departmentName;
    
	public EmployeeDTO() {
		super();
	}
    
	public EmployeeDTO(int id, String name, String email, String role, String departmentName) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.departmentName = departmentName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

