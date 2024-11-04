package com.classroom.equipment.dao;

import com.classroom.equipment.entity.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificationRecipientDao extends JpaRepository<NotificationRecipient, Integer> {
}
