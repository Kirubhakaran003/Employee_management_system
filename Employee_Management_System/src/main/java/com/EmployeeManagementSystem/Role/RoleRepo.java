package com.EmployeeManagementSystem.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
	Optional<Role>findByRole(String role);
}
