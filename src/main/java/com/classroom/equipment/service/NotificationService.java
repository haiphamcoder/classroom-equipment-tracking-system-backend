package com.classroom.equipment.service;

import com.classroom.equipment.service.impl.MyTelegramBot;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final EmailService emailService;

    private final MyTelegramBot telegramBotAdapter;

    public NotificationService(EmailService emailService,
                               MyTelegramBot telegramBotAdapter) {
        this.emailService = emailService;
        this.telegramBotAdapter = telegramBotAdapter;
    }
}