package com.classroom.equipment.service;

import com.classroom.equipment.dtos.request.AddEquipmentRoomRequest;
import com.classroom.equipment.entity.EquipmentRoom;

public interface EquipmentRoomService {
    String addEquipmentRoom(AddEquipmentRoomRequest request);

    EquipmentRoom getEquipmentRoomById(Long id);
}
