package com.classroom.equipment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "notification_schedule")
public class NotificationSchedule extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private BorrowOrder order;

    private String recipientEmail;

    private String recipientTelegramId;

    private String notificationContent;

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private LocalDateTime createdTime;
}
