package com.classroom.equipment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "staff")
@SQLRestriction(value = "is_deleted = false")
public class Staff extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "building_id", referencedColumnName = "id")
    private Building buildingId;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private boolean admin = false;

    @Column(nullable = false)
    private boolean firstLogin = true;
}
