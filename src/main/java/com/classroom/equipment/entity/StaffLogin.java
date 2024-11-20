package com.classroom.equipment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity(name = "staff_login")
public class StaffLogin {
    @Id
    @Column(name = "staff_id")
    @JsonProperty("staff_id")
    private Long staffId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    private Staff staff;

    @Column(name = "username", nullable = false, unique = true)
    @JsonProperty("username")
    private String username;

    @Column(name = "salt", nullable = false)
    @JsonProperty("salt")
    private String salt;

    @Column(name = "password_hash", nullable = false)
    @JsonProperty("password_hash")
    private String passwordHash;

    @Column(name = "reset_token")
    @JsonProperty("reset_token")
    private String resetToken;

    @Column(name = "token_expiration")
    @JsonProperty("token_expiration")
    private LocalDateTime tokenExpiration;

    @Column(name = "last_login")
    @JsonProperty("last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_at", nullable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdTime;

    @Column(name = "updated_at", nullable = false)
    @JsonProperty("updated_at")
    private LocalDateTime updatedTime;
}
