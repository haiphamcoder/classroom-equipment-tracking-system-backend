package com.classroom.equipment.dtos.export;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import com.classroom.equipment.common.enums.OrderStatus;
import com.classroom.equipment.dtos.response.BorrowOrderResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BorrowOrderExportDTO {
    private Long id;
    private String borrowerName;
    private String staffName;
    private LocalDateTime borrowTime;
    private LocalDateTime returnDeadline;
    private OrderStatus status;
    private String items;

    public static BorrowOrderExportDTO from(BorrowOrderResponse order) {
        String itemsStr = order.getItems().stream()
            .map(item -> String.format("%s (x%d)", 
                item.getEquipmentName(), 
                item.getQuantity()))
            .collect(Collectors.joining(", "));

        return BorrowOrderExportDTO.builder()
            .id(order.getId())
            .borrowerName(order.getBorrowerName())
            .staffName(order.getStaffName())
            .borrowTime(order.getBorrowTime())
            .returnDeadline(order.getReturnDeadline())
            .status(order.getStatus())
            .items(itemsStr)
            .build();
    }
}
