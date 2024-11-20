package com.classroom.equipment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@Entity(name = "borrowers")
public class Borrowers {
    @Id
    @Column(name = "borrower_id")
    @JsonProperty("borrower_id")
    private Long borrowerId;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "type", nullable = false)
    @JsonProperty("type")
    private int type;

    @Column(name = "email", nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @Column(name = "telegram_id")
    @JsonProperty("telegram_id")
    private String telegramId;

    @Column(name = "locked")
    @JsonProperty("locked")
    private boolean locked;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private int status;
}
