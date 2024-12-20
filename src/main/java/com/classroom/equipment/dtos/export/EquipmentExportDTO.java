package com.classroom.equipment.dtos.export;

import com.classroom.equipment.common.enums.EquipmentStatus;
import com.classroom.equipment.entity.Equipment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EquipmentExportDTO {
    private Long id;
    private String name;
    private String roomName;
    private Integer quantity;
    private EquipmentStatus status;

    public static EquipmentExportDTO from(Equipment equipment) {
        return EquipmentExportDTO.builder()
            .id(equipment.getId())
            .name(equipment.getName())
            .roomName(equipment.getRoom().getRoomName())
            .quantity(equipment.getQuantity())
            .status(equipment.isDeleted() ? EquipmentStatus.UNAVAILABLE : equipment.getStatus())
            .build();
    }
} 