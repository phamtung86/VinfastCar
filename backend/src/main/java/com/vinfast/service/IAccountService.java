package com.vinfast.service;

import com.vinfast.entity.Account;

public interface IAccountService {
    Account findAccountByUsername(String username);
}
