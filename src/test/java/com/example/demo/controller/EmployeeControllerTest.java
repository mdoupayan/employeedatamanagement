package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.controller.EmployeeController;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        // Mock data
        List<EmployeeDTO> employeeList = Arrays.asList(
                new EmployeeDTO(1, "doupayan", "doupayan@gmail.com", "HR", "HR"),
                new EmployeeDTO(2, "rashmi", "rashmi@gmail.com", "HR", "HR")
        );

        // Mock service layer
        when(employeeService.getAllEmployeeList()).thenReturn(employeeList);

        // Perform GET request
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("doupayan"))
                .andExpect(jsonPath("$[1].name").value("rashmi"));
    }

    @Test
    public void testSaveEmployee() throws Exception {
        EmployeeRequest request = new EmployeeRequest();
        request.setName("sumehs");
        request.setEmail("sumesh@example.com");
        request.setDepartmentName("IT");
        request.setPassword("password");
        request.setRole("IT");

        EmployeeDTO savedEmployee = new EmployeeDTO(3, "sumehs", "sumesh@example.com", "IT", "IT");

        // Mock service layer
        when(employeeService.saveEmployee(any(EmployeeRequest.class))).thenReturn(savedEmployee);

        // Perform POST request
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"sumehs\", \"email\":\"sumesh@example.com\", \"departmentName\":\"IT\", \"password\":\"password\", \"role\":\"IT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("sumehs"));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        int employeeId = 1;
        EmployeeRequest request = new EmployeeRequest();
        request.setName("John Smith");
        request.setEmail("john.smith@example.com");
        request.setDepartmentName("Finance");
        request.setPassword("newpassword");
        request.setRole("ROLE_USER");

        EmployeeDTO updatedEmployee = new EmployeeDTO(employeeId, "John Smith", "john.smith@example.com", "ROLE_USER", "Finance");

        // Mock service layer
        when(employeeService.updateEmployee(any(EmployeeRequest.class), eq(employeeId)))
                .thenReturn(updatedEmployee);

        // Perform PUT request
        mockMvc.perform(put("/employees/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\", \"email\":\"john.smith@example.com\", \"departmentName\":\"Finance\", \"password\":\"newpassword\", \"role\":\"ROLE_USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        int employeeId = 1;

        // Mock service layer for successful deletion
        mockMvc.perform(delete("/employees/" + employeeId))
                .andExpect(status().isOk());  // Check for status 200 OK
    }

    @Test
    public void testDeleteEmployeeNotFound() throws Exception {
        int employeeId = 99;

        // Mock service layer to throw exception
        doThrow(new NoSuchElementException("Employee not found with ID: " + employeeId))
                .when(employeeService).deleteEmployee(employeeId);

        // Perform DELETE request and expect NOT_FOUND status
        mockMvc.perform(delete("/employees/" + employeeId))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetEmployee() throws Exception {
        int employeeId = 1;
        EmployeeDTO employee = new EmployeeDTO(employeeId, "John Doe", "john.doe@example.com", "ROLE_USER", "Finance");

        // Mock service layer
        when(employeeService.getEmployee(employeeId)).thenReturn(employee);

        // Perform GET request
        mockMvc.perform(get("/employees/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testGetEmployeeNotFound() throws Exception {
        int employeeId = 99;

        // Mock service layer to throw exception
        doThrow(new NoSuchElementException("Employee not found with ID: " + employeeId))
                .when(employeeService).getEmployee(employeeId);

        // Perform GET request and expect NOT_FOUND status
        mockMvc.perform(get("/employees/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
