package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DepartmentRequest;
import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
	
	@Autowired
	DepartmentService departmentService;
	
	@GetMapping
	public List<Department> getAllDepartment() {
		return departmentService.getAllDepartment();
	}
	
	@PostMapping
	public Department saveDepartment(@RequestBody DepartmentRequest departmentRequest) {
		return departmentService.saveDepartment(departmentRequest);
	}
	
	@PutMapping("/{id}")
	public Department updateDepartment(@PathVariable int id, @RequestBody DepartmentRequest departmentRequest) {
		return departmentService.updateDepartment(departmentRequest, id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteDepartment(@PathVariable int id) {
		departmentService.deleteDepartment(id);
	}

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)  // Sets HTTP status to 404
	public String handleNotFoundException(NoSuchElementException ex) {
		return ex.getMessage(); // You can return a custom message or an error response
	}
}
