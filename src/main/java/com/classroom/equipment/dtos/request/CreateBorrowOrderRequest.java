package com.classroom.equipment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBorrowOrderRequest {
    private Long borrowerId;
    private Long staffId;
    private LocalDateTime borrowTime;
    private LocalDateTime returnDeadline;
    private List<OrderItemRequest> items;
}