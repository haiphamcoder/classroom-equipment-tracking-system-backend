package com.classroom.equipment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBuildingRequest {
    private Long id;
    private String buildingName;
    private Boolean available;
}
