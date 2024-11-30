package com.EmployeeManagementSystem.Controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeManagementSystem.AppUser.AppUser;
import com.EmployeeManagementSystem.AppUser.AppUserService;
import com.EmployeeManagementSystem.Model.Employee;
import com.EmployeeManagementSystem.Model.EmployeeService;
import com.EmployeeManagementSystem.Role.Role;
import com.EmployeeManagementSystem.Role.RoleService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
public class EmployeeController {
	
	@Autowired
	EmployeeService empService;
	
	@Autowired
	AppUserService appService;
	
	@Autowired
	RoleService roleService;
	
	PasswordEncoder en = new BCryptPasswordEncoder();
	
	/**
	 * 
	 * @param authentication
	 * @param auth
	 * @param role
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static boolean checkAuthorization(Authentication authentication, SecurityContextHolder auth, String role) {
		boolean roleFound = false;

		// who is currently logged in
		System.out.println("Currently logged in by : " + authentication.getName());

		// find the role of the person who have logged in
		Collection<? extends GrantedAuthority> grantedRoles = auth.getContext().getAuthentication().getAuthorities();

		for (int i = 0; i < grantedRoles.size(); i++) {
			String roleAccepted = grantedRoles.toArray()[i].toString();
			System.out.println("My role : " + role);

			if (roleAccepted.equalsIgnoreCase(role)) {
				roleFound = true;
			}
		}

		return roleFound;
	}
	
	/**
	 * 
	 * @param authentication
	 * @param auth
	 * @param id
	 * @param role
	 * @return
	 */
	@PostMapping("api/addRole")
	public String addRole(Authentication authentication, SecurityContextHolder auth,@RequestParam int id ,@RequestParam String role) {
		boolean check=checkAuthorization(authentication, auth, "admin");
		if(check) {
			Role r=new Role(id, role);
			roleService.add(r);
			return "Role Added";
		}
		return "Access Denied";
	}
	
	/**
	 * 
	 * @param authentication
	 * @param auth
	 * @param id
	 * @param fname
	 * @param lname
	 * @param email
	 * @return
	 */
	@PostMapping("api/addEmployee")
	public String add(Authentication authentication, SecurityContextHolder auth,@RequestParam int id,@RequestParam String fname, @RequestParam String lname, @RequestParam String email) {
		boolean check=checkAuthorization(authentication, auth, "admin");
		if(check) {
		Employee e1=new Employee(id, fname, lname, email);
		empService.add(e1);
		return "Data Added";
	}
		else {
			return "access denied";
		}
	}
	
	/**
	 * 
	 * @param authentication
	 * @param auth
	 * @param id
	 * @param name
	 * @param email
	 * @param password
	 * @param authority
	 * @return
	 */
	@PostMapping("api/addUser")
	public String addUser(Authentication authentication, SecurityContextHolder auth,@RequestParam int id,@RequestParam String name,@RequestParam String email,@RequestParam String password,
			@RequestParam String[] authority )
	{
		boolean check=checkAuthorization(authentication, auth, "admin");
		if(check) {
			List<String> str = Arrays.asList(authority);
			Set<String> se=new HashSet<>(str);
			AppUser a1=new AppUser(id, name, email, en.encode(password), se);
			appService.add(a1);
		return "User Added";
		}
		else {
			return "Access Denied";
		}
	}
	
	/**
	 * 
	 * @param authentication
	 * @param auth
	 * @param id
	 * @param fname
	 * @param lname
	 * @param email
	 * @return
	 */
	@PutMapping("api/updateEmployee")
	public String update(Authentication authentication, SecurityContextHolder auth,@RequestParam int id,@RequestParam String fname, @RequestParam String lname, @RequestParam String email) {
		boolean check=checkAuthorization(authentication, auth, "admin");
		if(check) {
		Employee e1=new Employee(id, fname, lname, email);
		empService.add(e1);
		return "Data Updated";
		}
		else {
			return "access denied";
		}
	}
	
	/**
	 * 
	 * @param id
	 * @param authentication
	 * @param auth
	 * @return
	 */
	@DeleteMapping("api/deleteEmployee")
	public String delete(@RequestParam int id,Authentication authentication, SecurityContextHolder auth) {
		boolean check=checkAuthorization(authentication, auth, "admin");
		if(check) {
		Employee e1=empService.getById(id);
		empService.delete(e1);
		return "Data Deleted";
		}
		else {
			return "access denied";
		}
	}

	@GetMapping("api/viewEmployees")
	public List<Employee>getAll(){
		return empService.getAll();
	}
	
	@Operation(summary = "desc for sort in descending order and asc for ascending order")
	@GetMapping("api/sortEmployee")
	public List<Employee>getBySort(@RequestParam String sort){
		if(sort.equalsIgnoreCase("desc")) {
		return empService.getBySort("fname",Direction.DESC);
		}
		else {
			return empService.getBySort("fname", Direction.ASC);
		}
	}
	
	@Operation(summary = "Enter Employee Id to get the employee details")
	@GetMapping("api/getEmployeeById")
	public Employee getById(@RequestParam int id) {
		Employee e1=empService.getById(id);
		return e1;
	}
	
	@Operation(summary="Enter First name of employee to search ")
	@GetMapping("api/searchByFname")
	public List<Employee> searchByName(@RequestParam String fname){
		List<Employee>e1=empService.getByFname(fname);
		return e1;
	}
	
}
