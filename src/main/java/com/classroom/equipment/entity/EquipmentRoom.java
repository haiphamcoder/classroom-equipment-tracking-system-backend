package com.classroom.equipment.entity;

import com.classroom.equipment.common.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "equipment_room")
@SQLRestriction(value = "is_deleted = false")
public class EquipmentRoom extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "building_id", referencedColumnName = "id")
    private Building building;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Staff manager;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
