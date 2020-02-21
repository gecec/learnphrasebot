package ru.gecec.learnphrasebot.bot.commands;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.gecec.learnphrasebot.bot.service.SecurityService;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

import java.util.Arrays;
import java.util.List;

public class ListCardsCommand extends BotCommand implements BasicCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(ListCardsCommand.class);

    private static final String LOGTAG = "LISTCARDSCOMMAND";

    private final CardRepository cardRepository;
    private final SecurityService securityService;

    public ListCardsCommand(final CardRepository cardRepository, final SecurityService securityService) {
        super("list", "With this command you list cards");
        this.cardRepository = cardRepository;
        this.securityService = securityService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (!securityService.isAdmin(user.getUserName())) {
            LOGGER.error(String.format("Unauthorized user %s tries to create card", user.getUserName()));
            sendMessage(chat.getId().toString(), absSender, String.format("У вас нет прав на создание карточки"));
            return;
        }

        StringBuilder builder = new StringBuilder();
        //TODO check for message length (4096 symbols)
        cardRepository.getAllCards()
                .stream()
                .forEach(card -> builder
                        .append(card.getTranslation())
                        .append(" : ")
                        .append(card.getWord())
                        .append("\r\n"));

        sendMessage(chat.getId().toString(), absSender, builder.toString());
    }

    @Override
    public String getLogtag() {
        return LOGTAG;
    }
}
