package com.classroom.equipment.service;

import com.classroom.equipment.entity.Staff;

public interface StaffService {

    Staff getStaffById(Long id);

    String createStaffAccount(Staff staff);
}
