package com.classroom.equipment.entity;

import com.classroom.equipment.common.enums.BorrowerType;
import com.classroom.equipment.common.enums.Status;
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

    @Column(nullable = false, unique = true)
    private String email;

    private String telegramId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private String note;

}
