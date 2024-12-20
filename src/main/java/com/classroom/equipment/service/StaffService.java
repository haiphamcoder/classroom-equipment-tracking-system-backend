package com.classroom.equipment.service;

import com.classroom.equipment.dtos.request.ChangePasswordRequest;
import com.classroom.equipment.dtos.request.CreateStaffRequest;
import com.classroom.equipment.dtos.request.LoginRequest;
import com.classroom.equipment.dtos.request.UpdateStaffRequest;
import com.classroom.equipment.entity.Staff;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface StaffService {

    Staff getStaffById(Long id);

    String createStaffAccount(CreateStaffRequest request);

    String updateStaffAccount(UpdateStaffRequest request);

    String deleteStaffAccount(Long id);

    List<Staff> findAllStaff();

    Staff login(LoginRequest loginRequest);

    String changePassword(ChangePasswordRequest changePasswordRequest);

    ResponseEntity<Resource> exportStaffs(String format);
}
