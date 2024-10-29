package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.DepartmentRequest;
import com.example.demo.entity.Department;

public interface DepartmentService {
	
	List<Department> getAllDepartment();
	
	Department updateDepartment(DepartmentRequest departmentRequest, int id);
	
	void deleteDepartment(int id);
	
	Department saveDepartment(DepartmentRequest departmentRequest);

}
