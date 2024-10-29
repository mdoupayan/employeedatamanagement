package com.example.demo.service.impl;

import java.util.List;
import java.util.NoSuchElementException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DepartmentRequest;
import com.example.demo.entity.Department;
import com.example.demo.repo.DepartmentRepo;
import com.example.demo.service.DepartmentService;
import com.example.demo.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

	private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

	@Autowired
	private DepartmentRepo departmentRepo;

	@Override
	public List<Department> getAllDepartment() {
		logger.info("Fetching all departments");
		return departmentRepo.findAll();
	}

	@Override
	public Department updateDepartment(DepartmentRequest departmentRequest, int id) {
		try {
			Department dept = departmentRepo.findById(id)
					.orElseThrow(() -> new NoSuchElementException("Department not found with ID: " + id));

			dept.setName(departmentRequest.getName().toUpperCase());
			Department updatedDept = departmentRepo.save(dept);
			logger.info("Updated department with ID: {}", id);
			return updatedDept;

		} catch (NoSuchElementException e) {
			logger.error("Error updating department with ID: {}", id, e);
			throw e;  // or throw a custom exception
		}
	}

	@Override
	public void deleteDepartment(int id) {
		try {
			departmentRepo.deleteById(id);
			logger.info("Deleted department with ID: {}", id);
		} catch (NoSuchElementException e) {
			logger.error("Error deleting department with ID: {}", id, e);
			throw e;  // or throw a custom exception
		}
	}

	@Override
	public Department saveDepartment(DepartmentRequest departmentRequest) {
		Department dept = new Department();
		dept.setName(departmentRequest.getName().toUpperCase());
		Department savedDept = departmentRepo.save(dept);
		logger.info("Saved new department with ID: {}", savedDept.getId());
		return savedDept;
	}
}


