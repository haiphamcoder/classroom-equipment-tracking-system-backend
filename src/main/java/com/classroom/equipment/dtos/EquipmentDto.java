package com.classroom.equipment.dtos;

import lombok.Data;

@Data
public class EquipmentDto {
    private String name;
    private long roomId;
    private int quantity;
}
