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
@Entity(name = "notification_schedule")
public class NotificationSchedule {
    @Id
    @Column(name = "notification_id")
    @JsonProperty("notification_id")
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private BorrowOrders order;

    @Column(name = "recipient_email")
    @JsonProperty("recipient_email")
    private String recipientEmail;

    @Column(name = "recipient_telegram_id")
    @JsonProperty("recipient_telegram_id")
    private String recipientTelegramId;

    @Column(name = "notification_content")
    @JsonProperty("notification_content")
    private String notificationContent;

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private int status;

    @Column(name = "created_at", nullable = false)
    @JsonProperty("created_time")
    private LocalDateTime createdTime;
}
