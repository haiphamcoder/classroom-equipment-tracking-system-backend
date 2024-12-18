package com.classroom.equipment.dtos.response;

import com.classroom.equipment.common.enums.EquipmentStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    private Long id;
    private String equipmentName;
    private String equipmentRoomName;
    private Integer quantity;
    private EquipmentStatus status;
    private String notes;
}
