package ru.gecec.learnphrasebot.bot.commands.handler;

import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class CommandHandler {
    public abstract String handle(Message message);
}
