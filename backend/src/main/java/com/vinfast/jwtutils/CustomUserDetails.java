package com.vinfast.jwtutils;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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


}
