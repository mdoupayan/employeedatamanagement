package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Employee;
import com.example.demo.repo.EmployeeRepo;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	EmployeeRepo employeeRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Employee emp = employeeRepo.findByEmail(email);
		if (emp == null) {
			throw new UsernameNotFoundException("Employee not found");
		}
		return new CustomUserDetails(emp);
	}

}
