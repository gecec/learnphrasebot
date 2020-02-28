package ru.gecec.learnphrasebot.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public interface BasicCommand {
    default void sendMessage(String chatId, AbsSender absSender, String text) throws TelegramApiException {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId);
        answer.setText(text);
        absSender.execute(answer);
    }
}
