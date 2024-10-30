package com.classroom.equipment.layer.infrastructure.telegram;

import com.classroom.equipment.dtos.TelegramMessageType;

public interface ITelegramBotAdapter {
    void sendMessage(String message, TelegramMessageType tupe);
}
