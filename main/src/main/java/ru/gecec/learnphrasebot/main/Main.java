package ru.gecec.learnphrasebot.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gecec.learnphrasebot.bot.CommandBot;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

@SpringBootApplication
@EnableScheduling
@ImportResource("classpath:spring/context-main.xml")
public class Main implements CommandLineRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @Autowired
    private CommandBot commandBot;

    @Value("${proxy.enabled}")
    private boolean isProxyEnabled;

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
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("telegram", "telegram".toCharArray());
            }
        });

        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

//        if (isProxyEnabled) {
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            botOptions.setProxyHost("sr123.spry.fail");
            botOptions.setProxyPort(1080);
//        }

        return botOptions;
    }
}
