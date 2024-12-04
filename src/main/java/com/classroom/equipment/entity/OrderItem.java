package com.classroom.equipment.entity;

import com.classroom.equipment.common.enums.EquipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "order_time")
@SQLRestriction(value = "is_deleted = false")
public class OrderItem extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private BorrowOrder order;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    private EquipmentStatus status;

    private Integer quantity;

    private String notes;

    private LocalDateTime returnTime;
} 