package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.constant.CommonConstants;
import com.classroom.equipment.config.ApiException;
import com.classroom.equipment.dtos.export.StaffExportDTO;
import com.classroom.equipment.dtos.request.ChangePasswordRequest;
import com.classroom.equipment.dtos.request.CreateStaffRequest;
import com.classroom.equipment.dtos.request.LoginRequest;
import com.classroom.equipment.dtos.request.UpdateStaffRequest;
import com.classroom.equipment.entity.Building;
import com.classroom.equipment.entity.Staff;
import com.classroom.equipment.entity.StaffLogin;
import com.classroom.equipment.repository.BuildingRepository;
import com.classroom.equipment.repository.StaffLoginRepository;
import com.classroom.equipment.repository.StaffRepository;
import com.classroom.equipment.service.EmailService;
import com.classroom.equipment.service.ExportService;
import com.classroom.equipment.service.StaffService;
import com.classroom.equipment.utils.PasswordUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final StaffLoginRepository staffLoginRepository;
    private final BuildingRepository buildingRepository;
    private final PasswordUtils passwordUtils;
    private final EmailService emailService;
    private final ExportService exportService;

    @Override
    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).orElseThrow(
            () -> new ApiException("Staff not found")
        );
    }

    @Override
    @Transactional
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
            .firstLogin(true)
            .build();

        staffRepository.save(staff);

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

        emailService.sendEmail(
            request.getEmail(),
            "Classroom Equipment Tracking System - Tài Khoản Mới",
            body, CommonConstants.EMAIL_STAFF_ACCOUNT_CREATION_TEMPLATE
        );

        return "Staff account created successfully";
    }


    @Override
    @Transactional
    public String updateStaffAccount(UpdateStaffRequest request) {
        Staff staff = staffRepository.findById(request.getId())
            .orElseThrow(() -> new ApiException("Staff not found"));

        if (StringUtils.hasText(request.getBuildingName())) {
            Building building = buildingRepository.findByBuildingName(request.getBuildingName())
                .orElseThrow(() -> new ApiException("Building not found"));
            staff.setBuildingId(building);
        }
        if (StringUtils.hasText(request.getName())) {
            staff.setName(request.getName());
        }
        if (StringUtils.hasText(request.getPhone())) {
            staff.setPhone(request.getPhone());
        }

        staffRepository.save(staff);
        
        return "Staff account updated successfully";
    }

    @Override
    public String deleteStaffAccount(Long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(
            () -> new ApiException("Staff not found")
        );
        staff.setDeleted(true);
        staffRepository.save(staff);

        StaffLogin staffLogin = staffLoginRepository.findByStaffId(id).orElseThrow(
            () -> new ApiException("Staff not found")
        );
        staffLogin.setDeleted(true);
        staffLoginRepository.save(staffLogin);

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

        Staff staff = staffLogin.getStaff();
        
        if (staffLogin.getLastLogin() != null) {
            staffLogin.setFirstLogin(false);
            staff.setFirstLogin(false);
            staffRepository.save(staff);
        }
        
        staffLogin.setLastLogin(LocalDateTime.now());
        staffLoginRepository.save(staffLogin);

        return staff;
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

    @Override
    public ResponseEntity<Resource> exportStaffs(String format) {
        List<Staff> staffs = staffRepository.findAll();
        
        List<StaffExportDTO> exportData = staffs.stream()
            .map(StaffExportDTO::from)
            .collect(Collectors.toList());
        
        String filename = String.format("Staff_List_%s", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss")));
            
        return exportService.exportToFile(exportData, filename, format);
    }

}
