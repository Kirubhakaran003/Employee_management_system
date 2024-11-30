package com.EmployeeManagementSystem.AppUser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser, Integer>{

	//create custom method using JPA
	Optional<AppUser> findByName(String name);
	
}
