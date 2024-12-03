package com.classroom.equipment.entity;

import com.classroom.equipment.common.enums.EquipmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "equipment")
@SQLRestriction(value = "is_deleted = false")
public class Equipment extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private EquipmentRoom room;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    @Column(nullable = false)
    private int quantity;
}
