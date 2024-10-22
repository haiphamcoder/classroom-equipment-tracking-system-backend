package com.classroom.equipment.config.telegram;

import com.classroom.equipment.config.telegram.properties.TelegramBotProperties;
import com.classroom.equipment.layer.infrastructure.telegram.impl.TelegramBotAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean("telegramBotSender")
    public TelegramBotAdapter telegramBotSender(@Qualifier("telegramBotProperties") TelegramBotProperties telegramBotProperties,
                                                @Value("${spring.application.name}") String sender) {
        return new TelegramBotAdapter(telegramBotProperties, sender);
    }
}
