package com.classroom.equipment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "account")
public class Account {
    @Id
    private Long id;

    @Column(name = "full_name")
    @JsonProperty("full_name")
    private String fullName;

    @Column(name = "telegram_id")
    @JsonProperty("telegram_id")
    private String telegramId;

    @Column(name = "email")
    @JsonProperty("email")
    private String email;
}
