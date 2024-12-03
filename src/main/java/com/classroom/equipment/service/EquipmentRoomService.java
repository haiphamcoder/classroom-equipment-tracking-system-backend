package com.classroom.equipment.service;

import com.classroom.equipment.dtos.EquipmentRoomDto;
import com.classroom.equipment.entity.EquipmentRoom;

public interface EquipmentRoomService {
    void addEquipmentRoom(EquipmentRoomDto equipmentRoomDto);

    EquipmentRoom getEquipmentRoomById(Long id);
}
