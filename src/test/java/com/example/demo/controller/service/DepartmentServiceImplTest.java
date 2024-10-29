package com.example.demo.controller.service;

import com.example.demo.dto.DepartmentRequest;
import com.example.demo.entity.Department;
import com.example.demo.repo.DepartmentRepo;
import com.example.demo.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Mock
    private DepartmentRepo departmentRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllDepartment() {
        Department dept1 = new Department();
        dept1.setId(1);
        dept1.setName("HR");

        Department dept2 = new Department();
        dept2.setId(2);
        dept2.setName("Finance");

        when(departmentRepo.findAll()).thenReturn(Arrays.asList(dept1, dept2));

        List<Department> departments = departmentService.getAllDepartment();

        assertEquals(2, departments.size());
        verify(departmentRepo, times(1)).findAll();
    }

    @Test
    public void testUpdateDepartment() {
        int departmentId = 1;
        Department existingDept = new Department();
        existingDept.setId(departmentId);
        existingDept.setName("HR");

        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName("Finance");

        when(departmentRepo.findById(departmentId)).thenReturn(Optional.of(existingDept));
        when(departmentRepo.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Department updatedDept = departmentService.updateDepartment(departmentRequest, departmentId);

        assertEquals("FINANCE", updatedDept.getName());
        verify(departmentRepo, times(1)).findById(departmentId);
        verify(departmentRepo, times(1)).save(existingDept);
    }

    @Test
    public void testUpdateDepartmentNotFound() {
        int departmentId = 99;
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName("Finance");

        when(departmentRepo.findById(departmentId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> departmentService.updateDepartment(departmentRequest, departmentId));
        verify(departmentRepo, times(1)).findById(departmentId);
    }

    @Test
    public void testDeleteDepartment() {
        int departmentId = 1;

        doNothing().when(departmentRepo).deleteById(departmentId);

        departmentService.deleteDepartment(departmentId);

        verify(departmentRepo, times(1)).deleteById(departmentId);
    }

    @Test
    public void testDeleteDepartmentNotFound() {
        int departmentId = 99;

        doThrow(new NoSuchElementException("Department not found with ID: " + departmentId)).when(departmentRepo).deleteById(departmentId);

        assertThrows(NoSuchElementException.class, () -> departmentService.deleteDepartment(departmentId));
        verify(departmentRepo, times(1)).deleteById(departmentId);
    }

    @Test
    public void testSaveDepartment() {
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName("HR");

        Department savedDept = new Department();
        savedDept.setId(1);
        savedDept.setName("HR");

        when(departmentRepo.save(any(Department.class))).thenReturn(savedDept);

        Department result = departmentService.saveDepartment(departmentRequest);

        assertEquals("HR", result.getName());
        assertEquals(1, result.getId());
        verify(departmentRepo, times(1)).save(any(Department.class));
    }
}
