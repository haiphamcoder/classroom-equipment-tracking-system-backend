package com.classroom.equipment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateStaffRequest {
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String buildingName;
}
