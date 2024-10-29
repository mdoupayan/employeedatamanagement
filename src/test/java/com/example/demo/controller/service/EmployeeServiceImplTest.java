package com.example.demo.controller.service;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.repo.DepartmentRepo;
import com.example.demo.repo.EmployeeRepo;

import com.example.demo.service.impl.EmployeeServiceImpl;
import com.example.demo.utils.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepo employeeRepo;

    @Mock
    private DepartmentRepo departmentRepo;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private CommonUtils commonUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployeeList() {
        Department department = new Department();
        department.setId(1);
        department.setName("HR");

        Employee emp1 = new Employee(1, "doupayan", "doupayan@gmail.com", "doupayanPassword", "HR", new Department(1,"HR"));
        emp1.setDepartment(department);

        Employee emp2 = new Employee(2, "rashmi", "rashmi@gmail.com", "doupayanPassword", "HR", new Department(1,"HR"));
        emp2.setDepartment(department);

        when(employeeRepo.findAll()).thenReturn(Arrays.asList(emp1, emp2));

        List<EmployeeDTO> employees = employeeService.getAllEmployeeList();

        assertEquals(2, employees.size());
        assertEquals("doupayan@gmail.com", employees.get(0).getEmail());
        verify(employeeRepo, times(1)).findAll();
    }

    @Test
    public void testSaveEmployee() {
        Department department = new Department();
        department.setId(1);
        department.setName("HR");

        EmployeeRequest employeeRequest = new EmployeeRequest("doupayan", "doupayan@gmail.com", "password", "HR","HR");
        when(departmentRepo.findByName("HR")).thenReturn(department);
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");

        Employee emp = new Employee();
        emp.setId(1);
        emp.setName("Doupayan");
        emp.setEmail("doupayan@gmail.com");
        emp.setPassword("hashedPassword");
        emp.setRole("HR");
        emp.setDepartment(department);

        when(employeeRepo.save(any(Employee.class))).thenReturn(emp);

        EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeRequest);

        assertEquals("doupayan@gmail.com", savedEmployee.getEmail());
        assertEquals("Doupayan", savedEmployee.getName());
        verify(departmentRepo, times(1)).findByName("HR");
        verify(passwordEncoder, times(1)).encode("password");
        verify(employeeRepo, times(1)).save(any(Employee.class));
    }

    @Test
    public void testDeleteEmployee() {
        int employeeId = 1;

        doNothing().when(employeeRepo).deleteById(employeeId);

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepo, times(1)).deleteById(employeeId);
    }

    @Test
    public void testDeleteEmployeeNotFound() {
        int employeeId = 99;

        doThrow(new NoSuchElementException("Employee not found with ID: " + employeeId)).when(employeeRepo).deleteById(employeeId);

        assertThrows(NoSuchElementException.class, () -> employeeService.deleteEmployee(employeeId));
        verify(employeeRepo, times(1)).deleteById(employeeId);
    }

    @Test
    public void testUpdateEmployee() {
        int employeeId = 1;
        Department department = new Department();
        department.setId(1);
        department.setName("HR");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setName("Doupayan");
        existingEmployee.setEmail("doupayan@gmail.com");
        existingEmployee.setRole("HR");
        existingEmployee.setPassword("doupayanpassword");
        existingEmployee.setDepartment(department);

        EmployeeRequest employeeRequest = new EmployeeRequest("Doupayan", "updated@gmail.com", "doupayanpassword", "HR","HR");

        when(employeeRepo.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(departmentRepo.findByName("HR")).thenReturn(department);

        when(employeeRepo.save(existingEmployee)).thenReturn(existingEmployee);

        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeRequest, employeeId);


        assertEquals("updated@gmail.com", updatedEmployee.getEmail());
        verify(employeeRepo, times(1)).findById(employeeId);
        verify(departmentRepo, times(1)).findByName("HR");

    }

    @Test
    public void testUpdateEmployeeNotFound() {
        int employeeId = 99;
        EmployeeRequest employeeRequest = new EmployeeRequest("Doupayan Updated", "updated@gmail.com", "newPassword", "HR","HR");

        when(employeeRepo.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> employeeService.updateEmployee(employeeRequest, employeeId));
        verify(employeeRepo, times(1)).findById(employeeId);
    }

    @Test
    public void testGetEmployee() {
        int employeeId = 1;
        Department department = new Department();
        department.setId(1);
        department.setName("HR");

        Employee emp = new Employee();
        emp.setId(employeeId);
        emp.setName("Doupayan");
        emp.setEmail("doupayan@gmail.com");
        emp.setRole("HR");
        emp.setDepartment(department);

        when(employeeRepo.findById(employeeId)).thenReturn(Optional.of(emp));

        EmployeeDTO employeeDTO = employeeService.getEmployee(employeeId);

        assertEquals("Doupayan", employeeDTO.getName());
        assertEquals("doupayan@gmail.com", employeeDTO.getEmail());
        verify(employeeRepo, times(1)).findById(employeeId);
    }

    @Test
    public void testGetEmployeeNotFound() {
        int employeeId = 99;

        when(employeeRepo.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> employeeService.getEmployee(employeeId));
        verify(employeeRepo, times(1)).findById(employeeId);
    }
}