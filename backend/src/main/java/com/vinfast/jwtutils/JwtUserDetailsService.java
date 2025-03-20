package com.vinfast.jwtutils;

import com.vinfast.entity.Account;
import com.vinfast.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private IAccountService accountService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else {
        	return new CustomUserDetails(account.getUserName(), account.getPassword(), account.getId(),account.getFirstName(),account.getLastName(),account.getRole(), AuthorityUtils.createAuthorityList(account.getRole()));
        }
    }
}
