package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/employeelist")
	public String showEmployeeListPage() {
		return "employeelist";
	}
	
	@GetMapping("/updateEmployeeForm")
    public String updateEmployeeForm() {
        return "updateEmployee";
    }
	
	@GetMapping("/addEmployeeForm")
    public String addEmployeeForm() {
        return "addEmployee";
    }
	
	@GetMapping("/departmentlist")
    public String showDepartmentlist() {
        return "departmentlist";
    }
	
}
