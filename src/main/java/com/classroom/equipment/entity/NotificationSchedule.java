package com.classroom.equipment.entity;

import com.classroom.equipment.common.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "notification_schedule")
@SQLRestriction(value = "is_deleted = false")
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
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column(name = "executed_time", nullable = false)
    private LocalDateTime executedTime;
}
