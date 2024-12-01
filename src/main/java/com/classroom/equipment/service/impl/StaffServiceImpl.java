package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.constant.CommonConstants;
import com.classroom.equipment.config.ApiException;
import com.classroom.equipment.dtos.request.ChangePasswordRequest;
import com.classroom.equipment.dtos.request.CreateStaffRequest;
import com.classroom.equipment.dtos.request.LoginRequest;
import com.classroom.equipment.entity.Staff;
import com.classroom.equipment.entity.StaffLogin;
import com.classroom.equipment.repository.BuildingRepository;
import com.classroom.equipment.repository.StaffLoginRepository;
import com.classroom.equipment.repository.StaffRepository;
import com.classroom.equipment.service.EmailService;
import com.classroom.equipment.service.StaffService;
import com.classroom.equipment.utils.PasswordUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final String LOGIN_URL = "35.185.177.130/login";
    private final StaffRepository staffRepository;
    private final StaffLoginRepository staffLoginRepository;
    private final BuildingRepository buildingRepository;
    private final PasswordUtils passwordUtils;
    private final EmailService emailService;

    @Override
    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).orElseThrow(
            () -> new ApiException("Staff not found")
        );
    }

    @Override
    public String createStaffAccount(CreateStaffRequest request) {
        Optional<Staff> staffOpt = staffRepository.findByName(request.getUsername());
        if (staffOpt.isPresent()) {
            throw new ApiException("Username already exist");
        }

        String tempPassword = passwordUtils.generatePassword();
        String salt = passwordUtils.generateSalt();

        Staff staff = Staff.builder()
            .name(request.getFullName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .buildingId(buildingRepository.findByBuildingName(request.getBuildingName())
                .orElseThrow(() -> new ApiException("Building not found")))
            .build();

        staff = staffRepository.save(staff);

        StaffLogin staffLogin = StaffLogin.builder()
            .staff(staff)
            .username(request.getUsername())
            .salt(salt)
            .passwordHash(passwordUtils.hashPassword(tempPassword, salt))
            .firstLogin(true)
            .build();

        staffLoginRepository.save(staffLogin);

        Map<String, Object> body = new HashMap<>();
        body.put("fullName", request.getFullName());
        body.put("username", request.getUsername());
        body.put("password", tempPassword);
        body.put("loginUrl", LOGIN_URL);

        emailService.sendEmail(
            request.getEmail(),
            "Classroom Equipment Tracking System - Tài Khoản Mới",
            body, CommonConstants.EMAIL_STAFF_ACCOUNT_CREATION_TEMPLATE
        );

        return "Staff account created successfully";
    }


    @Override
    public String updateStaffAccount(Long staffId, Staff staff) {
        return "";
    }

    @Override
    public String deleteStaffAccount(Long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(
            () -> new ApiException("Staff not found")
        );
        staff.setDeleted(true);
        staffRepository.save(staff);

        return "Staff account deleted successfully";
    }

    @Override
    public List<Staff> findAllStaff() {
        List<Staff> staffList = staffRepository.findAll();
        staffList.sort(Comparator.comparing(Staff::getId));

        return staffList;
    }

    @Override
    public Staff login(LoginRequest loginRequest) {
        StaffLogin staffLogin = staffLoginRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new ApiException("Invalid username or password"));

        if (!passwordUtils.verifyPassword(
                loginRequest.getPassword(), 
                staffLogin.getSalt(), 
                staffLogin.getPasswordHash())) {
            throw new ApiException("Invalid username or password");
        }

        if (staffLogin.getLastLogin() == null) {
            staffLogin.setFirstLogin(false);
        }
        staffLogin.setLastLogin(LocalDateTime.now());
        staffLoginRepository.save(staffLogin);

        return staffLogin.getStaff();
    }

    @Override
    public String changePassword(ChangePasswordRequest request) {
        StaffLogin staffLogin = staffLoginRepository.findById(request.getStaffId())
            .orElseThrow(() -> new ApiException("Staff not found"));

        if (!passwordUtils.verifyPassword(
            request.getOldPassword(),
            staffLogin.getSalt(),
            staffLogin.getPasswordHash())) {
            throw new ApiException("Current password is incorrect");
        }

        String newSalt = passwordUtils.generateSalt();
        String newPasswordHash = passwordUtils.hashPassword(request.getNewPassword(), newSalt);

        staffLogin.setSalt(newSalt);
        staffLogin.setPasswordHash(newPasswordHash);

        staffLoginRepository.save(staffLogin);

        return "Password changed successfully";
    }

}
