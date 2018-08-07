package ru.gecec.learnphrasebot.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import ru.gecec.learnphrasebot.bot.CommandBot;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@SpringBootApplication
@ImportResource("classpath:spring/context-main.xml")
public class Main implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String LOGTAG = "MAINTAG";

    @Autowired
    private CommandBot commandBot;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.debug("Hello");

        try {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("telegram", "telegram".toCharArray());
                }
            });

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            try {
                telegramBotsApi.registerBot(commandBot);
            } catch (TelegramApiException ex) {
                BotLogger.error(LOGTAG, ex);
            }
        } catch (Exception ex) {
            BotLogger.error(LOGTAG, ex);
        }
    }

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        botOptions.setProxyHost("sr123.spry.fail");
        botOptions.setProxyPort(1080);
        return botOptions;
    }
}
