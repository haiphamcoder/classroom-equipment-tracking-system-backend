package com.classroom.equipment.dtos.request;

import com.classroom.equipment.common.enums.EquipmentStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReturnRequest {
    private Long orderId;
    private Long staffId;
    private List<ReturnItemRequest> items;

    @Data
    public static class ReturnItemRequest {
        private Long orderItemId;
        private Integer returnQuantity;
        private EquipmentStatus status;
        private String notes;
    }
}