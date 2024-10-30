package com.classroom.equipment.layer.infrastructure.telegram.impl;

import com.classroom.equipment.config.telegram.properties.TelegramBotProperties;
import com.classroom.equipment.dtos.TelegramMessageType;
import com.classroom.equipment.layer.infrastructure.telegram.ITelegramBotAdapter;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class TelegramBotAdapter implements ITelegramBotAdapter {
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot%s/sendMessage";
    private static final String BODY_FORMAT = "{\"chat_id\": \"%s\", \"text\": \"%s\", \"parse_mode\": \"HTML\"}";
    private final String sender;
    private final String url;
    private final String chatId;
    private final AsyncHttpClient asyncHttpClient;

    public TelegramBotAdapter(TelegramBotProperties telegramBotProperties,
                              String sender) {
        this.url = String.format(TELEGRAM_API_URL, telegramBotProperties.getToken());
        this.chatId = "1653490505";
        this.asyncHttpClient = Dsl.asyncHttpClient();
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message, TelegramMessageType type) {
        CompletableFuture<String> response = send(message, type);
        response.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send message", ex);
            } else {
                log.info(result);
            }
        });
    }

    private CompletableFuture<String> send(String message, TelegramMessageType type) {
        String formattedMessage = formatMessage(message, "cet_system", type);
        String body = String.format(BODY_FORMAT, chatId, formattedMessage);

        try {
            return asyncHttpClient.preparePost(url)
                    .setHeader("Content-Type", "application/json")
                    .setBody(body)
                    .execute()
                    .toCompletableFuture().thenApply(response -> {
                        if (response.getStatusCode() == 200) {
                            return "Message sent successfully";
                        } else {
                            return "Failed to send message";
                        }
                    });
        } catch (Exception e) {
            CompletableFuture<String> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    private String formatMessage(String message, String sender, TelegramMessageType type) {
        return switch (type) {
            case INFO -> String.format("ℹ️ <b><i>From: %s</i></b>\n%s", sender, message);
            case WARNING -> String.format("⚠️ <b><i>From: %s</i></b>\n%s", sender, message);
            case ERROR -> String.format("⛔️ <b><i>From: %s</i></b>\n%s", sender, message);
            default -> message;
        };
    }
}
