package com.classroom.equipment.service;

import com.classroom.equipment.common.constant.CommonConstants;
import com.classroom.equipment.dtos.EmailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {
    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
    public void testEmailService() {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(Collections.singletonList("ngochai285nd@gmail.com"));
        emailMessage.setSubject("Test email service");
        emailMessage.setModel(new HashMap<>(Map.of("name", "Phạm Ngọc Hải", "content", "Đây chỉ là email test")));
        emailService.sendEmail(emailMessage, CommonConstants.EMAIL_NOTIFICATION_TEMPLATE);
    }
}