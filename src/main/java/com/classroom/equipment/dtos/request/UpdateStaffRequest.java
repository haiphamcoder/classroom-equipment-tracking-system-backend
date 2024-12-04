package com.classroom.equipment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStaffRequest {
    private Long id;
    private String name;
    private String buildingName;
    private String phone;
}
