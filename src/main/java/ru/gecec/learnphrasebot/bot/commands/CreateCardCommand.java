package ru.gecec.learnphrasebot.bot.commands;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.gecec.learnphrasebot.bot.CardCreationException;
import ru.gecec.learnphrasebot.bot.service.CardService;
import ru.gecec.learnphrasebot.model.entity.Card;

import java.util.Arrays;
import java.util.List;

public class CreateCardCommand extends BotCommand implements BasicCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(CreateCardCommand.class);

    private final static List<String> admins = Arrays.asList("gecec", "Ksuha_muha");
    private static final String LOGTAG = "CREATECARDCOMMAND";

    private CardService cardService;

    public CreateCardCommand(final CardService cardService) {
        super("c", "With this command you can create new word card");
        this.cardService = cardService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (!admins
                .stream()
                .anyMatch(admin -> user.getUserName().equals(admin))
        ) {
            LOGGER.error(LOGTAG, String.format("Unauthorized user %s tries to create card", user.getUserName()));
            sendMessage(chat.getId().toString(), absSender, String.format("У вас нет прав на создание карточки"));
            return;
        }

        try {
            Card card = cardService.createCardFromTemplate(String.join("", strings));
            sendMessage(chat.getId().toString(), absSender, String.format("Успешно создана карточка %s", card.toString()));
        } catch (CardCreationException ex) {
            sendMessage(chat.getId().toString(), absSender, ex.getMessage());
            return;
        }
    }


    @Override
    public String getLogtag() {
        return LOGTAG;
    }
}
