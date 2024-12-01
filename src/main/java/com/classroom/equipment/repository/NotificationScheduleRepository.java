package com.classroom.equipment.repository;

import com.classroom.equipment.entity.NotificationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationScheduleRepository extends JpaRepository<NotificationSchedule, Long> {
}
