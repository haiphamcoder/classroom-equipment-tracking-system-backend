package com.classroom.equipment.service;

import com.classroom.equipment.common.enums.NotificationStatus;
import com.classroom.equipment.entity.NotificationSchedule;
import com.classroom.equipment.repository.NotificationScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationSchedulerService {
    private final NotificationScheduleRepository notificationScheduleRepository;

    public NotificationSchedulerService (NotificationScheduleRepository notificationScheduleRepository) {
        this.notificationScheduleRepository = notificationScheduleRepository;
    }

    public NotificationSchedule createNotificationSchedule(NotificationSchedule notificationSchedule) {
        return notificationScheduleRepository.save(notificationSchedule);
    }

    public List<NotificationSchedule> findAllNotificationSchedule() {
        return notificationScheduleRepository.findAll();
    }

    public NotificationSchedule findNotificationScheduleById(Long id) {
        return notificationScheduleRepository.findById(id).orElse(null);
    }

    public List<NotificationSchedule> findAllNotificationScheduleByStatus(NotificationStatus status) {
        return notificationScheduleRepository.findAllByStatus(status);
    }

    public NotificationSchedule changeNotificationScheduleStatus(Long id, NotificationStatus status) {
        NotificationSchedule notificationSchedule = notificationScheduleRepository.findById(id).orElse(null);
        if (notificationSchedule != null) {
            notificationSchedule.setStatus(status);
            return notificationScheduleRepository.save(notificationSchedule);
        }
        return null;
    }

}
