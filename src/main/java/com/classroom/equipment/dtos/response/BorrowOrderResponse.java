package com.classroom.equipment.dtos.response;

import com.classroom.equipment.common.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowOrderResponse {
    private Long id;
    private String borrowerName;
    private String staffName;
    private LocalDateTime borrowTime;
    private LocalDateTime returnDeadline;
    private OrderStatus status;
    private List<OrderItemResponse> items;
}