package com.classroom.equipment.service;

import com.classroom.equipment.entity.Account;

public interface IAccountService {
    Account getAccountById(Long id);
    void saveAccount(Account account);
}
