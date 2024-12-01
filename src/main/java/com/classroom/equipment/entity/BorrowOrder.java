package com.classroom.equipment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "borrow_order")
public class BorrowOrder extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "borrower_id", referencedColumnName = "id")
    private Borrower borrower;

    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    private Staff staff;

    @Column(nullable = false)
    private LocalDateTime borrowTime;

    private LocalDateTime returnDeadline;

    @Column(nullable = false)
    private int status;
}
