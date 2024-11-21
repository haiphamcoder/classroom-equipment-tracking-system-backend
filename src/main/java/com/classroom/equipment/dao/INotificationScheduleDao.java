package com.classroom.equipment.dao;

import com.classroom.equipment.entity.NotificationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificationScheduleDao extends JpaRepository<NotificationSchedule, Long> {
}
