package com.classroom.equipment.service;

import com.classroom.equipment.dtos.request.AddEquipmentRequest;
import com.classroom.equipment.dtos.request.UpdateEquipmentRequest;
import com.classroom.equipment.entity.Equipment;

import java.util.List;

public interface EquipmentService {
    Equipment getEquipmentById(Long id);

    String createEquipment(AddEquipmentRequest request);

    List<Equipment> getAllEquipments();

    String updateEquipment(UpdateEquipmentRequest request);

    String deleteEquipment(Long id);

}
