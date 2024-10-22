package com.classroom.equipment.layer.infrastructure.telegram;

import com.classroom.equipment.layer.application.models.TelegramMessageType;

public interface ITelegramBotAdapter {
    void sendMessage(String message, TelegramMessageType tupe);
}
