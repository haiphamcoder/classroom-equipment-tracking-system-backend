package com.classroom.equipment.service.impl;

import com.classroom.equipment.dao.IAccountDao;
import com.classroom.equipment.entity.Account;
import com.classroom.equipment.service.IAccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {
    private final IAccountDao accountDao;

    public AccountService(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account getAccountById(Long id) {
        return accountDao.findById(id).orElse(null);
    }

    public void saveAccount(Account account) {
        accountDao.save(account);
    }


}
