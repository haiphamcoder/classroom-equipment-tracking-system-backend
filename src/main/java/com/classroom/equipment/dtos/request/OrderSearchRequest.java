package com.classroom.equipment.dtos.request;

import com.classroom.equipment.common.enums.OrderSortBy;
import com.classroom.equipment.common.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSearchRequest {
    private String borrowerName;
    private OrderStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private OrderSortBy sortBy;
    private String sortDirection;
} 