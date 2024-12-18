package com.classroom.equipment.repository;

import com.classroom.equipment.common.enums.NotificationStatus;
import com.classroom.equipment.entity.NotificationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationScheduleRepository extends JpaRepository<NotificationSchedule, Long> {
    List<NotificationSchedule> findAllByStatus(NotificationStatus status);
}
