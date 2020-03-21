package ru.gecec.learnphrasebot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gecec.learnphrasebot.bot.CommandBot;

@SpringBootApplication
@EnableScheduling
public class Main implements CommandLineRunner {
    private final static Logger log = LoggerFactory.getLogger(Main.class);

    @Autowired
    private CommandBot commandBot;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting bot...");

        try {
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

            try {
                telegramBotsApi.registerBot(commandBot);
                log.info("Bot started");
            } catch (TelegramApiException ex) {
                log.error(ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

}
