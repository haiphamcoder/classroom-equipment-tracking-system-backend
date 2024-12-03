package com.classroom.equipment.controller;

import com.classroom.equipment.common.constant.CommonConstants;
import com.classroom.equipment.dtos.EquipmentDto;

import com.classroom.equipment.entity.Equipment;
import com.classroom.equipment.entity.EquipmentRoom;
import com.classroom.equipment.service.EquipmentRoomService;
import com.classroom.equipment.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@RequiredArgsConstructor
public class EquipmentManagementController {
    private final EquipmentService equipmentService;
    private final EquipmentRoomService equipmentRoomService;

    @PostMapping("/add")
    public ResponseEntity<Equipment> addEquipment(@RequestBody EquipmentDto equipmentDto) {
        EquipmentRoom equipmentRoom = equipmentRoomService.getEquipmentRoomById(equipmentDto.getRoomId());
        if (equipmentRoom == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Equipment equipment = Equipment.builder()
                .name(equipmentDto.getName())
                .room(equipmentRoom)
                .quantity(equipmentDto.getQuantity())
                .status(CommonConstants.EQUIPMENT_AVAILABLE)
                .build();
        return ResponseEntity.ok(equipmentService.createEquipment(equipment));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }
}
