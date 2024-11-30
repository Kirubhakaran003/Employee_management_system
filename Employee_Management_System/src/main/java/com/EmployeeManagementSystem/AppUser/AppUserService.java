 	package com.EmployeeManagementSystem.AppUser;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService{

	@Autowired
	AppUserRepo repo;
	
	public void add (AppUser user) {
		repo.save(user);
	}
	public List<AppUser> getAll(){
		return repo.findAll();
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<AppUser> appUser=repo.findByName(username);
		
		Set<GrantedAuthority> granteAauthority=new HashSet<GrantedAuthority>();
		
		for(String temp:appUser.get().getAuthorities()) {
			GrantedAuthority ga=new SimpleGrantedAuthority(temp);
			granteAauthority.add(ga);
		}
		User user=new User(username,appUser.get().getPassword(),granteAauthority);
		return user;
	}


	public String login(String name, String password) {
		
		// find the user name to store in the appUser
		Optional<AppUser> appUser=repo.findByName(name);
		
		//save the userName as a normal object (user name find by optional function)\
		AppUser user=null;
		if(!appUser.isEmpty() ) {
			user=appUser.get();
		}
		
		PasswordEncoder en=new BCryptPasswordEncoder();
		
		if(user==null || en.matches(password, user.getPassword())==false) {
			
			return "Invalid Login";
		
		}
		else {
		return "Login Sucessfully";
		}
	}

}
