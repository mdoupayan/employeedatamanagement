package com.example.demo.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.repo.DepartmentRepo;
import com.example.demo.repo.EmployeeRepo;
import com.example.demo.service.EmployeeService;
import com.example.demo.utils.CommonUtils;
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private DepartmentRepo departmentRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private CommonUtils commonUtils;

	@Override
	public List<EmployeeDTO> getAllEmployeeList() {
		logger.info("Fetching all employees");
		List<Employee> employeeList = employeeRepo.findAll();

		return employeeList.stream()
				.map(employee -> new EmployeeDTO(
						employee.getId(),
						employee.getName(),
						employee.getEmail(),
						employee.getRole(),
						(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
				))
				.collect(Collectors.toList());
	}

	@Override
	public EmployeeDTO saveEmployee(EmployeeRequest employeeRequest) {
		logger.info("Saving a new employee with email: {}", employeeRequest.getEmail());
		Employee emp = new Employee();
		emp.setName(commonUtils.capitalizeWords(employeeRequest.getName()));

		Department dept = departmentRepo.findByName(employeeRequest.getDepartmentName().toUpperCase());
		if (dept == null) {
			logger.error("Department not found with name: {}", employeeRequest.getDepartmentName());
			throw new NoSuchElementException("Department not found: " + employeeRequest.getDepartmentName());
		}

		emp.setDepartment(dept);
		emp.setEmail(employeeRequest.getEmail());
		emp.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
		emp.setRole(commonUtils.capitalizeWords(employeeRequest.getRole()));

		emp = employeeRepo.save(emp);

		logger.info("Successfully saved employee with ID: {}", emp.getId());
		return new EmployeeDTO(
				emp.getId(),
				emp.getName(),
				emp.getEmail(),
				emp.getRole(),
				emp.getDepartment().getName()
		);
	}

	@Override
	public void deleteEmployee(int id) {
		logger.info("Deleting employee with ID: {}", id);
		try {
			employeeRepo.deleteById(id);
			logger.info("Successfully deleted employee with ID: {}", id);
		} catch (NoSuchElementException e) {
			logger.error("Employee not found with ID: {}", id, e);
			throw new NoSuchElementException("Employee not found with ID: " + id);
		}
	}

	@Override
	public EmployeeDTO updateEmployee(EmployeeRequest employeeRequest, int id) {
		logger.info("Updating employee with ID: {}", id);
		Employee emp = employeeRepo.findById(id)
				.orElseThrow(() -> {
					logger.error("Employee not found with ID: {}", id);
					return new NoSuchElementException("Employee not found with ID: " + id);
				});

		emp.setName(commonUtils.capitalizeWords(employeeRequest.getName()));

		Department dept = departmentRepo.findByName(employeeRequest.getDepartmentName().toUpperCase());
		if (dept == null) {
			logger.error("Department not found with name: {}", employeeRequest.getDepartmentName());
			throw new NoSuchElementException("Department not found: " + employeeRequest.getDepartmentName());
		}

		emp.setDepartment(dept);
		emp.setEmail(employeeRequest.getEmail());
		if (StringUtils.hasText(employeeRequest.getPassword())) {
			emp.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
		}
		emp.setRole(commonUtils.capitalizeWords(employeeRequest.getRole()));

		emp = employeeRepo.save(emp);

		logger.info("Successfully updated employee with ID: {}", emp.getId());
		return new EmployeeDTO(
				emp.getId(),
				emp.getName(),
				emp.getEmail(),
				emp.getRole(),
				emp.getDepartment().getName()
		);
	}

	@Override
	public EmployeeDTO getEmployee(int id) {
		logger.info("Fetching employee with ID: {}", id);
		Employee emp = employeeRepo.findById(id)
				.orElseThrow(() -> {
					logger.error("Employee not found with ID: {}", id);
					return new NoSuchElementException("Employee not found with ID: " + id);
				});

		logger.info("Successfully fetched employee with ID: {}", emp.getId());
		return new EmployeeDTO(
				emp.getId(),
				emp.getName(),
				emp.getEmail(),
				emp.getRole(),
				emp.getDepartment().getName()
		);
	}
}
