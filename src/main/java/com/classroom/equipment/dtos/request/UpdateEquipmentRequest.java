package com.classroom.equipment.dtos.request;

import com.classroom.equipment.common.enums.EquipmentStatus;
import lombok.Data;

@Data
public class UpdateEquipmentRequest {
    private Long id;
    private String name;
    private EquipmentStatus status;
    private Integer quantity;
}
