package com.classroom.equipment.entity;

import com.classroom.equipment.common.enums.EquipmentStatus;
import com.classroom.equipment.common.enums.ReturnStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "return_record")
@SQLRestriction(value = "is_deleted = false")
public class ReturnRecord extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private BorrowOrder order;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    private Staff staff;

    @Column(nullable = false)
    private LocalDateTime returnTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentStatus equipmentStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReturnStatus status;
}
