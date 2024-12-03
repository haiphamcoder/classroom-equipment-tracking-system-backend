package com.classroom.equipment.service;

import com.classroom.equipment.entity.Equipment;

import java.util.List;

public interface EquipmentService {
    Equipment getEquipmentById(Long id);

    Equipment createEquipment(Equipment equipment);

    List<Equipment> getAllEquipments();

    void updateEquipment(Long equipmentId, Equipment equipment);

    void deleteEquipment(Long id);

    void updateEquipmentStatus(Long equipmentId, int status);

    void updateEquipmentQuantity(Long equipmentId, int quantity);
}
