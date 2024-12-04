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
public class ExtendDeadlineRequest {
    private Long orderId;
    private LocalDateTime newDeadline;
} 
