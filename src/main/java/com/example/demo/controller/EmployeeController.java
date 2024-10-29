package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@GetMapping
	public List<EmployeeDTO> getAllEmployeeList() {
		return employeeService.getAllEmployeeList();
	}

	@PostMapping
	public EmployeeDTO saveEmployee(@RequestBody EmployeeRequest employeeRequest) {
		return employeeService.saveEmployee(employeeRequest);
	}

	@PutMapping("/{id}")
	public EmployeeDTO updateEmployee(@PathVariable int id, @RequestBody EmployeeRequest employeeRequest) {
		return employeeService.updateEmployee(employeeRequest, id);
	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable int id) {
		employeeService.deleteEmployee(id);
	}

	@GetMapping("/{id}")
	public EmployeeDTO getEmployee(@PathVariable int id) {
		return employeeService.getEmployee(id);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}
