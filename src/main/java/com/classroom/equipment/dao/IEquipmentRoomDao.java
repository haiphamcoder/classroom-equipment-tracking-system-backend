package com.classroom.equipment.dao;

import com.classroom.equipment.entity.EquipmentRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEquipmentRoomDao extends JpaRepository<EquipmentRoom, Long> {
}
