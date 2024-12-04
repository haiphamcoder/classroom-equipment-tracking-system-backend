package com.classroom.equipment.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBorrowOrderRequest {
    private Long borrowerId;
    private Long equipmentId;
    private Long staffId;
    private LocalDateTime borrowTime;
    private LocalDateTime returnDeadline;
} 