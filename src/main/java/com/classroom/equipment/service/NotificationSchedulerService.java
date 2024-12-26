package com.classroom.equipment.service;

import com.classroom.equipment.common.constant.CommonConstants;
import com.classroom.equipment.common.enums.NotificationStatus;
import com.classroom.equipment.entity.BorrowOrder;
import com.classroom.equipment.entity.NotificationSchedule;
import com.classroom.equipment.repository.BorrowerRepository;
import com.classroom.equipment.repository.NotificationScheduleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class NotificationSchedulerService {
    private final NotificationScheduleRepository notificationScheduleRepository;
    private final BorrowerRepository borrowerRepository;

    public NotificationSchedulerService (NotificationScheduleRepository notificationScheduleRepository,
                                         BorrowerRepository borrowerRepository) {
        this.notificationScheduleRepository = notificationScheduleRepository;
        this.borrowerRepository = borrowerRepository;
    }

    public NotificationSchedule createNotificationSchedule(NotificationSchedule notificationSchedule) {
        return notificationScheduleRepository.save(notificationSchedule);
    }

    public NotificationSchedule createNotificationScheduleFromBorrowOrder(BorrowOrder order){
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("borrowerName", order.getBorrower().getName());
        notificationData.put("ticketCode", order.getId());
        notificationData.put("borrowDate", order.getBorrowTime());
        notificationData.put("expectedReturnDate", order.getReturnDeadline());

        List<Map<String, Object>> equipmentList = new ArrayList<>();
        order.getOrderItems().forEach(orderItem -> {
            Map<String, Object> equipment = new HashMap<>();
            equipment.put("name", orderItem.getEquipment().getName());
            equipment.put("code", orderItem.getEquipment().getId());
            equipment.put("quantity", orderItem.getQuantity());
            equipmentList.add(equipment);
        });

        notificationData.put("devices", equipmentList);

        NotificationSchedule notificationSchedule = new NotificationSchedule();
        notificationSchedule.setRecipientEmail(order.getBorrower().getEmail());
        notificationSchedule.setRecipientTelegramId(order.getBorrower().getTelegramId());
        notificationSchedule.setStatus(NotificationStatus.PENDING);
        notificationSchedule.setOrder(order);
        notificationSchedule.setExecutedTime(order.getReturnDeadline().minusMinutes(10));

        try {
            String content = CommonConstants.OBJECT_MAPPER.writeValueAsString(notificationData);
            notificationSchedule.setNotificationContent(content);
        } catch (JsonProcessingException e) {
            log.error("Error creating notification schedule from borrow order", e);
            return null;
        }

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
