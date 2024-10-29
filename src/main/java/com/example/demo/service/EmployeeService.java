package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.EmployeeRequest;

public interface EmployeeService {

	List<EmployeeDTO> getAllEmployeeList();

	EmployeeDTO saveEmployee(EmployeeRequest employeeRequest);

	void deleteEmployee(int id);

	EmployeeDTO updateEmployee(EmployeeRequest employeeRequest, int id);

	EmployeeDTO getEmployee(int id);
}
