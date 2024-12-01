package com.classroom.equipment.service;

import com.classroom.equipment.dtos.request.CreateStaffRequest;
import com.classroom.equipment.dtos.request.LoginRequest;
import com.classroom.equipment.entity.Staff;

import java.util.List;

public interface StaffService {

    Staff getStaffById(Long id);

    String createStaffAccount(CreateStaffRequest request);

    String updateStaffAccount(Long staffId, Staff staff);

    String deleteStaffAccount(Long id);

    List<Staff> findAllStaff();

    Staff login(LoginRequest loginRequest);
}
