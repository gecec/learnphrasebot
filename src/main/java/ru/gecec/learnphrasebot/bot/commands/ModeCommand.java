package ru.gecec.learnphrasebot.bot.commands;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import static ru.gecec.learnphrasebot.bot.service.BotMode.RANDOM;
import static ru.gecec.learnphrasebot.bot.service.BotMode.HEBREW;
import static ru.gecec.learnphrasebot.bot.service.BotMode.RANDOM_HEBREW;
import static ru.gecec.learnphrasebot.bot.service.BotMode.RUSSIAN;

public class ModeCommand extends BotCommand implements BasicCommand {
    private final static Logger log = LoggerFactory.getLogger(ModeCommand.class);

    private SessionManager sessionManager;

    public ModeCommand(SessionManager sessionManager) {
        super("mode", "With this command you set language mode: RANDOM, HEBREW, RUSSIAN");
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            UserSession session = new UserSession(chat.getId(), user.getUserName());
            String messageText = "Вы не указали языковой режим";

            if (strings.length != 0) {
                String modeFromUser = strings[0];

                if (modeFromUser.equalsIgnoreCase(HEBREW.getValue())) {
                    sessionManager.setMode(session, HEBREW);
                    messageText = String.format("Задан языковой режим: %s", HEBREW);
                } else if (modeFromUser.equalsIgnoreCase(RUSSIAN.getValue())) {
                    sessionManager.setMode(session, RUSSIAN);
                    messageText = String.format("Задан языковой режим: %s", RUSSIAN);
                } else if (modeFromUser.equalsIgnoreCase(RANDOM.getValue())) {
                    sessionManager.setMode(session, RANDOM);
                    sessionManager.setRandomMode(session, RANDOM_HEBREW);
                    messageText = String.format("Задан языковой режим: %s", RANDOM);
                } else {
                    messageText = String.format("Неизвестный языковой режим: %s", modeFromUser);
                }
            }

            sendMessage(chat.getId().toString(), absSender, messageText);
        } catch (TelegramApiException ex){
            log.error(ex.getMessage(), ex);
        }
    }
}
