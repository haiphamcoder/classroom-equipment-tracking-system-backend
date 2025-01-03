package com.classroom.equipment.service.impl;

import com.classroom.equipment.common.constant.CommonConstants;
import com.classroom.equipment.config.telegram.properties.TelegramBotProperties;
import com.classroom.equipment.entity.BorrowOrder;
import com.classroom.equipment.entity.OrderItem;
import com.classroom.equipment.repository.BorrowOrderRepository;
import com.classroom.equipment.repository.NotificationRecipientRepository;
import com.classroom.equipment.repository.UserRepository;
import com.classroom.equipment.entity.NotificationRecipient;
import com.classroom.equipment.entity.User;
import com.classroom.equipment.dtos.UserSession;
import com.classroom.equipment.service.EmailService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MyTelegramBot implements LongPollingSingleThreadUpdateConsumer {
    private final Random random = new SecureRandom();
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final NotificationRecipientRepository notiRecipientRepository;
    private final BorrowOrderRepository borrowOrderRepository;
    private final TelegramClient telegramClient;

    private final Cache<String, UserSession> userSessions = CacheBuilder.newBuilder()
            .expireAfterWrite(CommonConstants.OTP_EXPIRATION_TIME + 1L, TimeUnit.MINUTES)
            .build();


    public MyTelegramBot(@Qualifier("telegramBotProperties") TelegramBotProperties telegramBotProperties,
                         EmailService emailService,
                         UserRepository userRepository,
                         NotificationRecipientRepository notiRecipientRepository,
                         BorrowOrderRepository borrowOrderRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.notiRecipientRepository = notiRecipientRepository;
        this.borrowOrderRepository = borrowOrderRepository;
        this.telegramClient = new OkHttpTelegramClient(telegramBotProperties.getToken());
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();

            UserSession userSession = userSessions.getIfPresent(chatId);
            if (userSession != null) {
                verificationUser(chatId, messageText);
            } else {
                checkCommand(chatId, messageText);
            }
        }
    }

    private void checkCommand(String chatId, String messageText) {
        switch (messageText) {
            case "/start" -> startCommand(chatId);
            case "/check" -> checkingOrder(chatId);
            case "/help" -> showHelp(chatId);
            default -> sendMessage(chatId, "Invalid command.\nPlease use /help to see available commands.");
        }
    }

    private void checkingOrder(String chatId){
        List<NotificationRecipient> recipients = notiRecipientRepository.findAllByTelegramId(chatId);
        if (recipients.isEmpty()) {
            sendMessage(chatId, "Your Telegram ID has not been registered.\nPlease use /start to register.");
        } else {
            List<BorrowOrder> orders = borrowOrderRepository.findAllByBorrowerId(recipients.getFirst().getUserId());
            if (orders.isEmpty()) {
                sendMessage(chatId, "No orders found.");
                return;
            }
            orders.forEach(order -> {
                List<OrderItem> orderItems = order.getOrderItems();
                StringBuilder message = new StringBuilder("Order ID: " + order.getId() + "\n");
                orderItems.forEach(item -> {
                    message.append("Item: ").append(item.getEquipment().getName()).append("\n")
                            .append("Quantity: ").append(item.getQuantity()).append("\n")
                            .append("Status: ").append(item.getStatus()).append("\n\n");
                });
                sendMessage(chatId, message.toString());
            });
        }
    }

    private void showHelp(String chatId) {
        String helpMessage = """
                Available commands:
                /start - Start the verification process
                /check - Check the status of orders
                /help - Show available commands
                """;
        sendMessage(chatId, helpMessage);
    }

    private void startCommand(String chatId) {
        userSessions.put(chatId, new UserSession());
        List<NotificationRecipient> recipients = notiRecipientRepository.findAllByTelegramId(chatId);
        if (!recipients.isEmpty()) {
            sendMessage(chatId, "Welcome back! Your Telegram ID has been registered.\nPlease use /help to see available commands.");
            return;
        }
        sendMessage(chatId, "Welcome! Please enter your ID: ");
    }

    private void verificationUser(String chatId, String input) {
        UserSession session = userSessions.getIfPresent(chatId);
        if (session != null) {
            if (!session.isWaitingOtp()) {
                sendOTP(session, chatId, input);
            } else {
                verifyOTP(session, chatId, input);
            }
        }
    }

    private void sendOTP(UserSession session, String chatId, String input) {
        User user;
        try {
            int userId = Integer.parseInt(input);
            user = userRepository.findById(userId).orElse(null);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Invalid format.\nPlease use /start to try again.");
            userSessions.invalidate(chatId);
            return;
        }
        if (user == null) {
            sendMessage(chatId, "USER NOT FOUND!\nPlease use /start to try again.");
            userSessions.invalidate(chatId);
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

            Map<String, Object> model = new HashMap<>();
            model.put("otp", otp);
            model.put("name", user.getName());
            model.put("expiryTime", CommonConstants.OTP_EXPIRATION_TIME);
            emailService.sendEmail(email, "Telegram Verification", model, CommonConstants.EMAIL_OTP_NOTIFICATION_TEMPLATE);
            sendMessage(chatId, "Hello, " + user.getName() + "!\nPlease check your email for the OTP.\n" +
                    "An OTP has been sent to your email: " + maskedEmail + ". Please enter it within 2 minutes.");
        }
    }

    private void verifyOTP(UserSession session, String chatId, String input) {
        if (input.equals(session.getOtp()) && session.isOtpValid()) {
            NotificationRecipient recipient = new NotificationRecipient();
            recipient.setUserId(session.getUserId());
            recipient.setTelegramId(chatId);
            recipient.setEmail(session.getEmail());
            notiRecipientRepository.save(recipient);
            userSessions.invalidate(chatId);
            sendMessage(chatId, "Verification successful!\nYour Telegram ID has been registered.");
        } else {
            sendMessage(chatId, "Invalid or expired OTP.\nPlease use /start to try again.");
            userSessions.invalidate(chatId);
        }
    }

    private String generateOTP() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendMessage(String chatId, String text) {
        sendMessage(chatId, text, null);
    }

    public void sendMessage(String chatId, String content, String parseMode){
        SendMessage message = new SendMessage(chatId, content);
        message.setParseMode(parseMode);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message to chatId: {}", chatId, e);
        }
    }
}
