package com.classroom.equipment.config.telegram;

import com.classroom.equipment.config.telegram.properties.TelegramBotProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {
    @Bean("telegramBotProperties")
    @ConfigurationProperties(prefix = "telegram.bot")
    public TelegramBotProperties telegramBotProperties() {
        return new TelegramBotProperties();
    }
}
