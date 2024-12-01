package com.classroom.equipment.controller;

import com.classroom.equipment.entity.Staff;
import com.classroom.equipment.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffManagementController {
    private final StaffService staffService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveAccount(@RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.createStaffAccount(staff));
    }
}
