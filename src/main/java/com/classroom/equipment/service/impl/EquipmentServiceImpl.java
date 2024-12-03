package com.classroom.equipment.service.impl;

import com.classroom.equipment.config.ApiException;
import com.classroom.equipment.entity.Equipment;
import com.classroom.equipment.repository.EquipmentRepository;
import com.classroom.equipment.service.EquipmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id).orElseThrow(
            () -> new ApiException("Equipment not found")
        );
    }

    @Override
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    @Override
    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @Override
    public void updateEquipment(Long equipmentId, Equipment equipment) {

    }

    @Override
    public void deleteEquipment(Long id) {

    }

    @Override
    public void updateEquipmentStatus(Long equipmentId, int status) {

    }

    @Override
    public void updateEquipmentQuantity(Long equipmentId, int quantity) {

    }
}
