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
@Entity(name = "building")
@SQLRestriction(value = "is_deleted = false")
public class Building extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String buildingName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
