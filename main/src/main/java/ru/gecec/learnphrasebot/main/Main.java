package ru.gecec.learnphrasebot.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gecec.learnphrasebot.bot.TestBot;

@SpringBootApplication
@ImportResource("classpath:spring/context-main.xml")
public class Main implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.debug("Hello");

        try {
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            try {
                // Register long polling bots. They work regardless type of TelegramBotsApi we are creating
                telegramBotsApi.registerBot(new TestBot());
            } catch (TelegramApiException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }
}
