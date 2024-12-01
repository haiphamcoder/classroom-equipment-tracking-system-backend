package com.classroom.equipment.controller;

import com.classroom.equipment.dtos.request.CreateStaffRequest;
import com.classroom.equipment.dtos.request.LoginRequest;
import com.classroom.equipment.entity.Staff;
import com.classroom.equipment.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffManagementController {
    private final StaffService staffService;

    @PostMapping("/login")
    public ResponseEntity<Staff> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(staffService.login(request));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffService.findAllStaff());
    }

    @PostMapping("/create")
    public ResponseEntity<String> saveAccount(@RequestBody CreateStaffRequest request) {
        return ResponseEntity.ok(staffService.createStaffAccount(request));
    }

    @PostMapping("update/{staffId}")
    public ResponseEntity<String> updateStaffAccount(@PathVariable Long staffId, @RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.updateStaffAccount(staffId, staff));
    }

    @PostMapping("delete/{staffId}")
    public ResponseEntity<String> deleteStaffAccount(@PathVariable Long staffId) {
        return ResponseEntity.ok(staffService.deleteStaffAccount(staffId));
    }
}
