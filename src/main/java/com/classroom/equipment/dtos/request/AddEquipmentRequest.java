package com.classroom.equipment.dtos.request;

import lombok.Data;

@Data
public class AddEquipmentRequest {
    private String name;
    private Long roomId;
    private int quantity;
}
