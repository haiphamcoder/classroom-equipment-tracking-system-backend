package com.classroom.equipment.controller;

import com.classroom.equipment.dtos.request.AddEquipmentRequest;

import com.classroom.equipment.dtos.request.AddEquipmentRoomRequest;
import com.classroom.equipment.dtos.request.UpdateEquipmentRequest;
import com.classroom.equipment.entity.Equipment;
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
    public ResponseEntity<String> addEquipment(@RequestBody AddEquipmentRequest request) {
        return ResponseEntity.ok(equipmentService.createEquipment(request));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateEquipment(@RequestBody UpdateEquipmentRequest request) {
        return ResponseEntity.ok(equipmentService.updateEquipment(request));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteEquipment(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.deleteEquipment(id));
    }

    @PostMapping("/room/add")
    public ResponseEntity<String> addEquipmentRoom(@RequestBody AddEquipmentRoomRequest request) {
        return ResponseEntity.ok(equipmentRoomService.addEquipmentRoom(request));
    }
}
