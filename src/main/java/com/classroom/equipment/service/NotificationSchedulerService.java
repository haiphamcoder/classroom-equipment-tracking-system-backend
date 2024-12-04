package com.classroom.equipment.service;

import com.classroom.equipment.repository.NotificationScheduleRepository;
import com.classroom.equipment.service.impl.MyTelegramBot;
import com.classroom.equipment.utils.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationSchedulerService {
    private final EmailService emailService;
    private final BorrowOrderService borrowOrderService;
    private final NotificationScheduleRepository notificationScheduleRepository;
    private final MyTelegramBot telegramBotAdapter;

    public NotificationSchedulerService(EmailService emailService,
                                        BorrowOrderService borrowOrderService,
                                        NotificationScheduleRepository notificationScheduleRepository,
                                        MyTelegramBot telegramBotAdapter) {
        this.emailService = emailService;
        this.borrowOrderService = borrowOrderService;
        this.notificationScheduleRepository = notificationScheduleRepository;
        this.telegramBotAdapter = telegramBotAdapter;
    }

    public void sendReminderNotification(String recipientEmail, String recipientTelegramId, Map<String, Object> data){
        if (!StringUtils.isNullOrEmpty(recipientTelegramId)){
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

        } else {
            String subject = "Nhắc nhở trả thiết bị";
            String template = "borrow-order-reminder";
            emailService.sendEmail(recipientEmail, subject, data, template);
        }
    }

    @Scheduled(fixedDelay = 30000L)
    public void deliverNotification(){

    }

    @Scheduled(initialDelay = 10000L, fixedDelay = Long.MAX_VALUE)
    public void test(){
        long borrowOrderId = 1;
        Map<String, Object> data = buildBodyModelCreateTicket(borrowOrderId);
        sendReminderNotification("ngochai285nd@gmail.com", null, data);
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
