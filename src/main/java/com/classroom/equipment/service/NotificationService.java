package com.classroom.equipment.service;

import com.classroom.equipment.common.constant.CommonConstants;
import com.classroom.equipment.common.enums.NotificationStatus;
import com.classroom.equipment.entity.NotificationSchedule;
import com.classroom.equipment.service.impl.MyTelegramBot;
import com.classroom.equipment.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
    private final EmailService emailService;
    private final NotificationSchedulerService notificationSchedulerService;
    private final MyTelegramBot telegramBotAdapter;

    public NotificationService(EmailService emailService,
                               NotificationSchedulerService notificationSchedulerService,
                               MyTelegramBot telegramBotAdapter) {
        this.emailService = emailService;
        this.notificationSchedulerService = notificationSchedulerService;
        this.telegramBotAdapter = telegramBotAdapter;
    }

    @Scheduled(fixedDelay = 30000L)
    public void deliverNotification() throws JsonProcessingException {
        List<NotificationSchedule> notificationSchedules = notificationSchedulerService.findAllNotificationScheduleByStatus(NotificationStatus.PENDING);
        for (NotificationSchedule notificationSchedule : notificationSchedules) {
            LocalDateTime executeTime = notificationSchedule.getExecutedTime();
            if (executeTime.isAfter(LocalDateTime.now())) {
                sendReminderNotifcation(notificationSchedule.getRecipientEmail(), notificationSchedule.getRecipientTelegramId(), notificationSchedule.getNotificationContent());
                notificationSchedulerService.changeNotificationScheduleStatus(notificationSchedule.getId(), NotificationStatus.SENT);
            }
        }
    }

    public void sendReminderNotifcation(String recipientEmail, String recipientTelegramId, String content) throws JsonProcessingException {
        Map<String, Object> data = CommonConstants.OBJECT_MAPPER.readValue(content, Map.class);
        if (!StringUtils.isNullOrEmpty(recipientTelegramId)) {
            String message = data.get("telegram").toString();
            telegramBotAdapter.sendMessage(recipientTelegramId, message, "Markdown");
        } else {
            String subject = data.get("subject").toString();
            String message = data.get("body").toString();
            emailService.sendEmail(recipientEmail, subject, message);
        }
    }

    public void sendReminderNotification(String recipientEmail, String recipientTelegramId, Map<String, Object> data) {
        if (!StringUtils.isNullOrEmpty(recipientTelegramId)) {
            StringBuilder message = new StringBuilder();
            message.append("📢 *Thông Báo Trả Thiết Bị* 📢\n\n")
                    .append("Xin chào, *").append(data.get("borrowerName")).append("*!\n\n")
                    .append("🎫 *Mã Ticket:* ").append(data.get("ticketCode")).append("\n")
                    .append("📅 *Thời gian mượn:* ").append(data.get("borrowDate")).append("\n")
                    .append("📅 *Thời gian trả dự kiến:* ").append(data.get("expectedReturnDate")).append("\n\n")
                    .append("🛠 *Danh sách thiết bị đã mượn:*\n");

            List<Map<String, Object>> devices = (List<Map<String, Object>>) data.get("devices");
            for (Map<String, Object> device : devices) {
                message.append("- ").append(device.get("name"))
                        .append(" (Mã: ").append(device.get("code"))
                        .append(") - Số lượng: ").append(device.get("quantity"))
                        .append("\n");
            }

            message.append("\n⚠️ *Lưu ý:* Vui lòng trả thiết bị đúng hạn vào ngày *")
                    .append(data.get("expectedReturnDate"))
                    .append("*. Nếu có bất kỳ vấn đề nào, hãy liên hệ với chúng tôi qua số điện thoại hoặc email.\n\n")
                    .append("Trân trọng,\n💻 *Đội ngũ Classroom Equipment Tracking System*");

            telegramBotAdapter.sendMessage(recipientTelegramId, message.toString(), "Markdown");

        }

        if (!StringUtils.isNullOrEmpty(recipientEmail)) {
            String subject = "Nhắc nhở trả thiết bị";
            String template = "borrow-order-reminder";
            emailService.sendEmail(recipientEmail, subject, data, template);
        }
    }

    @Scheduled(initialDelay = 10000L, fixedDelay = Long.MAX_VALUE)
    public void test() {
        long borrowOrderId = 1;
        Map<String, Object> data = buildBodyModelCreateTicket(borrowOrderId);
        sendReminderNotification("ngochai285nd@gmail.com", "1653490505", data);
    }


    private Map<String, Object> buildBodyModelCreateTicket(long borrowOrderId) {
        Map<String, Object> model = new HashMap<>();
        model.put("borrowerName", "Phạm Ngọc Hải");
        model.put("ticketCode", borrowOrderId);
        model.put("borrowDate", "");
        model.put("expectedReturnDate", "");

        List<Map<String, Object>> equipmentList = new ArrayList<>();
        Map<String, Object> equipment = new HashMap<>();
        equipment.put("name", "Laptop");
        equipment.put("code", "LAP001");
        equipment.put("quantity", 1);
        equipmentList.add(equipment);

        model.put("devices", equipmentList);
        return model;
    }
}