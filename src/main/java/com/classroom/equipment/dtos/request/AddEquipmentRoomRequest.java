package com.classroom.equipment.dtos.request;

import lombok.Data;

@Data
public class AddEquipmentRoomRequest {
    private String roomName;
    private long buildingId;
    private long managerId;
}
