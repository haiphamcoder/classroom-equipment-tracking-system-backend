package com.classroom.equipment.dao;

import com.classroom.equipment.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEquipmentDao extends JpaRepository<Equipment, Long> {
}
