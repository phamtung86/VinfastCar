package com.vinfast.service;

import com.vinfast.entity.Account;
import com.vinfast.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService{

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Account findAccountByUsername(String username) {
        return accountRepository.findByUserName(username);
    }
}
