package ru.gecec.learnphrasebot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gecec.learnphrasebot.bot.CommandBot;

@SpringBootApplication
@EnableScheduling
public class Main implements CommandLineRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Autowired
    private CommandBot commandBot;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Starting bot...");

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

            try {
                telegramBotsApi.registerBot(commandBot);
                LOGGER.info("Bot started");
            } catch (TelegramApiException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        /*botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        botOptions.setProxyHost("127.0.0.1");
        botOptions.setProxyPort(9150);*/

        return botOptions;
    }
}
