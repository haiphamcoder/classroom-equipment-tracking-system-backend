package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.enums.Status;
import com.classroom.equipment.config.exception.ApiException;
import com.classroom.equipment.dtos.request.AddEquipmentRoomRequest;
import com.classroom.equipment.entity.Building;
import com.classroom.equipment.entity.EquipmentRoom;
import com.classroom.equipment.entity.Staff;
import com.classroom.equipment.repository.BuildingRepository;
import com.classroom.equipment.repository.EquipmentRoomRepository;
import com.classroom.equipment.repository.StaffRepository;
import com.classroom.equipment.service.EquipmentRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EquipmentRoomServiceImpl implements EquipmentRoomService {
    private final EquipmentRoomRepository equipmentRoomRepository;
    private final BuildingRepository buildingRepository;
    private final StaffRepository staffRepository;

    @Override
    @Transactional
    public String addEquipmentRoom(AddEquipmentRoomRequest addEquipmentRoomRequest) {
        Building building = buildingRepository.findById(addEquipmentRoomRequest.getBuildingId())
                .orElseThrow(() -> new ApiException("Building not found"));

        Staff staff = staffRepository.findById(addEquipmentRoomRequest.getManagerId())
            .orElseThrow(() -> new ApiException("Staff not found"));

        EquipmentRoom equipmentRoom = EquipmentRoom.builder()
                .roomName(addEquipmentRoomRequest.getRoomName())
                .building(building)
                .manager(staff)
                .status(Status.AVAILABLE)
                .build();
        equipmentRoomRepository.save(equipmentRoom);

        return "Equipment Room Added Successfully";
    }

    @Override
    public EquipmentRoom getEquipmentRoomById(Long id) {
        return equipmentRoomRepository.findById(id)
                .orElse(null);
    }
}
