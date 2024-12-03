package com.classroom.equipment.dtos;

import lombok.Data;

@Data
public class EquipmentRoomDto {
    private String roomName;
    private long buildingId;
    private long managerId;
}
