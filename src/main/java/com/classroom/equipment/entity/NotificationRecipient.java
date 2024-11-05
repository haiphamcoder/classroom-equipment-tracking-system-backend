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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "notification_recipient")
public class NotificationRecipient {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email")
    @JsonProperty("email")
    private String email;

    @Column(name = "telegram_id")
    @JsonProperty("telegram_id")
    private String telegramId;
}
