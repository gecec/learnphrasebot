package ru.gecec.learnphrasebot.bot.commands;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.gecec.learnphrasebot.bot.service.CardService;
import ru.gecec.learnphrasebot.bot.service.SecurityService;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import java.util.Arrays;
import java.util.List;

import static ru.gecec.learnphrasebot.bot.commands.handler.CreateCommandEnum.WORD;

public class CreateCardCommand extends BotCommand implements BasicCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(CreateCardCommand.class);

    private static final String LOGTAG = "CREATECARDCOMMAND";

    private final CardService cardService;
    private final SessionManager sessionManager;
    private final SecurityService securityService;

    public CreateCardCommand(final CardService cardService, final SessionManager sessionManager, final SecurityService securityService) {
        super("c", "With this command you can create new word card");
        this.cardService = cardService;
        this.sessionManager = sessionManager;
        this.securityService = securityService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (!securityService.isAdmin(user.getUserName())) {
            LOGGER.error(LOGTAG, String.format("Unauthorized user %s tries to create card", user.getUserName()));
            sendMessage(chat.getId().toString(), absSender, String.format("У вас нет прав на создание карточки"));
            return;
        }

        UserSession userSession = new UserSession(chat.getId(), user.getUserName());
        if (!StringUtils.isEmpty(sessionManager.getCommand(userSession))) {
            sendMessage(chat.getId().toString(), absSender, "Вы уже находитесь в процессе создания карточки. Либо заполните карточку до конца. Либо отправьте stop чтобы остановить процесс");
            return;
        }

        sessionManager.setCommand(userSession, WORD);
        sendMessage(chat.getId().toString(), absSender, "Введите слово:");
    }


    @Override
    public String getLogtag() {
        return LOGTAG;
    }
}
