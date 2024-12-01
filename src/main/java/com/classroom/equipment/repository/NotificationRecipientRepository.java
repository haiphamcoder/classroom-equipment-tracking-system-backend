package com.classroom.equipment.repository;

import com.classroom.equipment.entity.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Integer> {
}
