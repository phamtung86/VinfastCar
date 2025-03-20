package com.vinfast.jwtutils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
@Getter
public class CustomUserDetails extends User {
    private Integer userId;

	private String firstName;

	private String lastName;

	private String fullName;

	private String role;

	public CustomUserDetails(String username, String password, Integer id,String firstName, String lastName,String role, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.userId = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = firstName +" "+ lastName;
		this.role = role;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
