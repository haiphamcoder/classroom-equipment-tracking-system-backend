package com.classroom.equipment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "staff_login")
public class StaffLogin extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    private Staff staff;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    private String passwordHash;

    private String resetToken;

    private LocalDateTime tokenExpiration;

    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private boolean firstLogin = true;
}
