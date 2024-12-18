package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.enums.EquipmentStatus;
import com.classroom.equipment.config.ApiException;
import com.classroom.equipment.dtos.request.AddEquipmentRequest;
import com.classroom.equipment.dtos.request.UpdateEquipmentRequest;
import com.classroom.equipment.entity.Equipment;
import com.classroom.equipment.entity.EquipmentRoom;
import com.classroom.equipment.repository.EquipmentRepository;
import com.classroom.equipment.service.EquipmentRoomService;
import com.classroom.equipment.service.EquipmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentRoomService equipmentRoomService;

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
    @Transactional
    public String createEquipment(AddEquipmentRequest request) {
        EquipmentRoom equipmentRoom = equipmentRoomService.getEquipmentRoomById(request.getRoomId());

        Equipment equipment = Equipment.builder()
            .name(request.getName())
            .room(equipmentRoom)
            .quantity(request.getQuantity())
            .status(EquipmentStatus.AVAILABLE)
            .build();

        equipmentRepository.save(equipment);

        return "Create equipment successfully";
    }

    @Override
    @Transactional
    public String updateEquipment(UpdateEquipmentRequest request) {
        Equipment equipment = getEquipmentById(request.getId());

        if (request.getName() != null) {
            equipment.setName(request.getName());
        }
        if (request.getStatus() != null) {
            equipment.setStatus(request.getStatus());
        }
        if (request.getQuantity() != null) {
            equipment.setQuantity(request.getQuantity());
        }
        equipmentRepository.save(equipment);

        return "Update equipment successfully";
    }

    @Override
    public String deleteEquipment(Long id) {
        Equipment equipment = getEquipmentById(id);
        equipment.setDeleted(true);
        equipmentRepository.save(equipment);

        return "Delete equipment successfully";
    }

}
