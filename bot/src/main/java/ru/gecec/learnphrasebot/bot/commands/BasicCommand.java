package ru.gecec.learnphrasebot.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

public interface BasicCommand {
    String getLogtag();

    default void sendMessage(String chatId, AbsSender absSender, String text) {
        try {
            SendMessage answer = new SendMessage();
            answer.setChatId(chatId);
            answer.setText(text);
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(getLogtag(), e);
        }
    }
}
