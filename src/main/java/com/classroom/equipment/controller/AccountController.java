package com.classroom.equipment.controller;

import com.classroom.equipment.entity.Account;
import com.classroom.equipment.service.IAccountService;
import com.classroom.equipment.utils.response.Response;
import com.classroom.equipment.utils.response.ResponseFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/get/{id}")
    public Response<Object> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        if (account != null) {
            return ResponseFactory.getSuccessResponse(null, account);
        } else {
            return ResponseFactory.getClientErrorResponse("Account not found");
        }
    }

    @PostMapping("/save")
    public Response<Object> saveAccount(@RequestBody Account account) {
        accountService.saveAccount(account);
        return ResponseFactory.getSuccessResponse("Account saved successfully");
    }
}
