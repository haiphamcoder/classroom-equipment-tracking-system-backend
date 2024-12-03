package com.classroom.equipment.service.impl;

import com.classroom.equipment.dtos.EquipmentRoomDto;
import com.classroom.equipment.entity.Building;
import com.classroom.equipment.entity.EquipmentRoom;
import com.classroom.equipment.entity.Staff;
import com.classroom.equipment.repository.BuildingRepository;
import com.classroom.equipment.repository.EquipmentRoomRepository;
import com.classroom.equipment.repository.StaffRepository;
import com.classroom.equipment.service.EquipmentRoomService;
import org.springframework.stereotype.Service;

@Service
public class EquipmentRoomServiceImpl implements EquipmentRoomService {
    private final EquipmentRoomRepository equipmentRoomRepository;
    private final BuildingRepository buildingRepository;
    private final StaffRepository staffRepository;

    public EquipmentRoomServiceImpl(EquipmentRoomRepository equipmentRoomRepository,
                                    BuildingRepository buildingRepository,
                                    StaffRepository staffRepository) {
        this.equipmentRoomRepository = equipmentRoomRepository;
        this.buildingRepository = buildingRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public void addEquipmentRoom(EquipmentRoomDto equipmentRoomDto) {
        Building building = buildingRepository.findById(equipmentRoomDto.getBuildingId())
                .orElse(null);
        if (building == null) {
            throw new RuntimeException("Building not found");
        }

        Staff staff = staffRepository.findById(equipmentRoomDto.getManagerId())
                .orElse(null);
        if (staff == null) {
            throw new RuntimeException("Staff not found");
        }

        EquipmentRoom equipmentRoom = EquipmentRoom.builder()
                .roomName(equipmentRoomDto.getRoomName())
                .building(building)
                .manager(staff)
                .build();
        equipmentRoomRepository.save(equipmentRoom);
    }

    @Override
    public EquipmentRoom getEquipmentRoomById(Long id) {
        return equipmentRoomRepository.findById(id)
                .orElse(null);
    }
}
