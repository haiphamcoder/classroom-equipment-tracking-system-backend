package com.classroom.equipment.dtos.export;

import com.classroom.equipment.entity.Staff;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffExportDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;

    public static StaffExportDTO from(Staff staff) {
        return StaffExportDTO.builder()
            .id(staff.getId())
            .name(staff.getName())
            .email(staff.getEmail())
            .phone(staff.getPhone())
            .role(staff.isAdmin() ? "Admin" : "Staff")
            .status(staff.isDeleted() ? "INACTIVE" : "ACTIVE")
            .build();
    }
} 