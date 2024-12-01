package com.classroom.equipment.service.impl;

import com.classroom.equipment.entity.Staff;
import com.classroom.equipment.repository.StaffRepository;
import com.classroom.equipment.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;

    @Override
    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public String createStaffAccount(Staff staff) {
        staffRepository.save(staff);

        return "Create account successful.";
    }

}
