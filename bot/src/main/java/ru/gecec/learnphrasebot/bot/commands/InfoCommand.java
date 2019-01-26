package ru.gecec.learnphrasebot.bot.commands;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

import java.util.Arrays;
import java.util.List;

public class InfoCommand extends BotCommand implements BasicCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(InfoCommand.class);
    private static final String LOGTAG = "INFOCOMMAND";

    public InfoCommand() {
        super("info", "With this command you list cards");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String currentStatus = "HEBREW";
        sendMessage(chat.getId().toString(), absSender, currentStatus);
    }

    @Override
    public String getLogtag() {
        return LOGTAG;
    }
}
