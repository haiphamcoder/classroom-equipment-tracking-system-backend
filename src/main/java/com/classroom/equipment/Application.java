package com.classroom.equipment;

import com.classroom.equipment.config.telegram.properties.TelegramBotProperties;
import com.classroom.equipment.service.impl.MyTelegramBot;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@EnableJpaAuditing
@Slf4j
public class Application implements CommandLineRunner {
    private final TelegramBotsLongPollingApplication botsApplication;
    private final TelegramBotProperties telegramBotProperties;
    private final MyTelegramBot myTelegramBot;

    public Application(@Qualifier("telegramBotProperties") TelegramBotProperties telegramBotProperties,
                       MyTelegramBot myTelegramBot) {
        this.telegramBotProperties = telegramBotProperties;
        this.myTelegramBot = myTelegramBot;
        this.botsApplication = new TelegramBotsLongPollingApplication();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        registerBot();
    }

    private void registerBot() {
        try {
            botsApplication.registerBot(telegramBotProperties.getToken(), myTelegramBot);
        } catch (TelegramApiException e) {
            log.error("Error while registering bot: ", e);
        }
    }
}
