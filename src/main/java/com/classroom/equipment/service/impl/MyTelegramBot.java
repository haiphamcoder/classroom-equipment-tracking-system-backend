package com.classroom.equipment.service.impl;

import com.classroom.equipment.dao.INotificationRecipientDao;
import com.classroom.equipment.dao.IUserDao;
import com.classroom.equipment.entity.NotificationRecipient;
import com.classroom.equipment.entity.User;
import com.classroom.equipment.dtos.UserSession;
import com.classroom.equipment.service.EmailService;
import com.classroom.equipment.utils.generator.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MyTelegramBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    private final Random random = new Random();
    private final SnowflakeIdGenerator idGenerator = SnowflakeIdGenerator.getInstance();

    private final Map<String, UserSession> userSessions = new ConcurrentHashMap<>();
    private final EmailService emailService;
    private final IUserDao userDao;
    private final INotificationRecipientDao notificationRecipientDao;

    public MyTelegramBot(EmailService emailService,
                         IUserDao userDao,
                         INotificationRecipientDao notificationRecipientDao) {
        this.emailService = emailService;
        this.userDao = userDao;
        this.notificationRecipientDao = notificationRecipientDao;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();

            if (userSessions.containsKey(chatId)) {
                handleUserInput(chatId, messageText);
            } else {
                checkCommand(chatId, messageText);
            }
        }
    }

    private void checkCommand(String chatId, String messageText) {
        switch (messageText) {
            case "/start" -> startCommand(chatId);
            case "/help" -> showHelp(chatId);
            default -> sendMessage(chatId, "Invalid command.\nPlease use /help to see available commands.");
        }
    }

    private void showHelp(String chatId) {
        String helpMessage = """
                Available commands:
                /start - Start the verification process
                /help - Show available commands
                """;
        sendMessage(chatId, helpMessage);
    }

    private void startCommand(String chatId) {
        userSessions.put(chatId, new UserSession());
        sendMessage(chatId, "Welcome! Please enter your ID: ");
    }

    private void handleUserInput(String chatId, String input) {
        UserSession session = userSessions.get(chatId);
        if (session != null) {
            if (!session.isWaitingOtp()) {
                User user;
                try {
                    int userId = Integer.parseInt(input);
                    user = userDao.findById(userId);
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Invalid format.\nPlease use /start to try again.");
                    userSessions.remove(chatId);
                    return;
                }
                if (user == null) {
                    sendMessage(chatId, "USER NOT FOUND!\nPlease use /start to try again.");
                    userSessions.remove(chatId);
                    return;
                } else {
                    session.setUserId(user.getId());
                    String email = user.getEmail();
                    session.setEmail(email);
                    String otp = generateOTP();
                    session.setOtp(otp);
                    session.setOtpGeneratedTime(LocalDateTime.now());
                    session.setWaitingOtp(true);

                    String emailPrefix = email.substring(0, email.indexOf('@'));
                    String emailPrefixMasked = emailPrefix.substring(0, emailPrefix.length() / 2) + "*".repeat(emailPrefix.length() - emailPrefix.length() / 2);
                    String maskedEmail = emailPrefixMasked + email.substring(email.indexOf('@'));
                    emailService.sendEmail(email, "Telegram Verification", "Your OTP is: " + otp);
                    sendMessage(chatId, "Hello, " + user.getName() + "!\nPlease check your email for the OTP.\n" +
                            "An OTP has been sent to your email: " + maskedEmail + ". Please enter it within 2 minutes.");
                }
            } else {
                if (input.equals(session.getOtp()) && session.isOtpValid()) {
                    NotificationRecipient recipient = new NotificationRecipient();
                    recipient.setUserId(session.getUserId());
                    recipient.setTelegramId(chatId);
                    recipient.setEmail(session.getEmail());
                    notificationRecipientDao.save(recipient);
                    userSessions.remove(chatId);
                    sendMessage(chatId, "Verification successful!\nYour Telegram ID has been registered.");
                } else {
                    sendMessage(chatId, "Invalid or expired OTP.\nPlease use /start to try again.");
                    userSessions.remove(chatId);
                }
            }
        }
    }

    private String generateOTP() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
