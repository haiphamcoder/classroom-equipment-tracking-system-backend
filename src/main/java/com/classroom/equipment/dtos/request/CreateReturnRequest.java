package com.classroom.equipment.dtos.request;

import com.classroom.equipment.common.enums.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReturnRequest {
    private Long orderId;
    private Long staffId;
    private EquipmentStatus equipmentStatus;
}