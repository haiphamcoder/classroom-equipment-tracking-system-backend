package com.classroom.equipment.entity;

import com.classroom.equipment.common.enums.BorrowerStatus;
import com.classroom.equipment.common.enums.BorrowerType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "borrower")
@SQLRestriction(value = "is_deleted = false")
public class Borrower extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BorrowerType type;

    @Column(nullable = false)
    private String email;

    private String telegramId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BorrowerStatus status;

    private String note;

}
