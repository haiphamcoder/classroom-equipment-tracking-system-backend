package com.classroom.equipment.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "borrower")
public class Borrower extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int type;

    @Column(nullable = false, unique = true)
    private String email;

    private String telegramId;

    private boolean locked;

    @Column(nullable = false)
    private int status;

}
