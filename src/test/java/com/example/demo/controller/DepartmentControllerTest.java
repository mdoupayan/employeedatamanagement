package com.example.demo.controller;


import com.example.demo.dto.DepartmentRequest;
import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DepartmentControllerTest {

    @InjectMocks
    private DepartmentController departmentController;

    @Mock
    private DepartmentService departmentService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }

    @Test
    public void testGetAllDepartments() throws Exception {
        Department department1 = new Department(1, "HR");
        Department department2 = new Department(4, "IT");
        List<Department> departments = Arrays.asList(department1, department2);

        when(departmentService.getAllDepartment()).thenReturn(departments);

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("HR"))
                .andExpect(jsonPath("$[1].name").value("IT"));

        verify(departmentService, times(1)).getAllDepartment();
    }

    @Test
    public void testSaveDepartment() throws Exception {
        DepartmentRequest departmentRequest = new DepartmentRequest("HR");
        Department savedDepartment = new Department(1, "HR");

        when(departmentService.saveDepartment(departmentRequest)).thenReturn(savedDepartment);

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"HR\"}")) // Use content() instead of body()
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("HR"));

        verify(departmentService, times(1)).saveDepartment(departmentRequest);
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        DepartmentRequest departmentRequest = new DepartmentRequest("HR");
        Department updatedDepartment = new Department(1, "HR");

        when(departmentService.updateDepartment(departmentRequest, 1)).thenReturn(updatedDepartment);

        mockMvc.perform( put("/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"HR\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("HR"));

        verify(departmentService, times(1)).updateDepartment(departmentRequest, 1);
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1);

        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().isOk());

        verify(departmentService, times(1)).deleteDepartment(1);
    }

    @Test
    public void testUpdateDepartment_NonExistent() throws Exception {
        // Arrange
        int departmentId = 99; // Assuming this ID does not exist
        DepartmentRequest departmentRequest = new DepartmentRequest("Finance");

        when(departmentService.updateDepartment(any(DepartmentRequest.class), eq(departmentId)))
                .thenThrow(new NoSuchElementException("Error updating department with ID: 99"));

        // Act & Assert
        mockMvc.perform(put("/departments/{id}", departmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Finance\"}"))
                .andExpect(status().isNotFound()); // Expecting 404 Not Found

        verify(departmentService, times(1)).updateDepartment(any(DepartmentRequest.class), eq(departmentId));
    }
}
