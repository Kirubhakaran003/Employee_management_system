package com.EmployeeManagementSystem.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	@Autowired
	RoleRepo repo;
	
	public void add(Role r) {
		repo.save(r);
	}
}