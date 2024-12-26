package com.classroom.equipment.repository;

import com.classroom.equipment.entity.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Integer> {
    List<NotificationRecipient> findAllByTelegramId(String telegramId);
}
