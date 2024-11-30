package com.EmployeeManagementSystem.Model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
	@Autowired
	EmployeeRepo repo;

	public void add(Employee emp) {
		repo.save(emp);
	}

	public void delete(Employee emp) {
		repo.delete(emp);
	}

	public void update(Employee emp) {
		repo.save(emp);
	}

	public List<Employee> getAll() {
		return repo.findAll();
	}

	public Employee getById(int id) {
		Optional<Employee> opt = repo.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	public List<Employee> getBySort(String columnName, Direction direction) {
		return repo.findAll(Sort.by(direction, columnName));
	}

	public List<Employee> getByFname(String searchkey) {

		// create dummy obj based on searchkey
		Employee dummy = new Employee();
		dummy.setFname(searchkey);

		// WHERE clause
		ExampleMatcher em = ExampleMatcher.matching()
				.withMatcher("fname", ExampleMatcher.GenericPropertyMatchers.contains())
				.withIgnorePaths("id", "lname", "email");

		// Combining dummy with WHERE
		Example<Employee> ex = Example.of(dummy, em);
		return repo.findAll(ex);
	}
}
