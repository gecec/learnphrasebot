package ru.gecec.learnphrasebot.bot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gecec.learnphrasebot.bot.service.CardService;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserSession;

public class StartCommand extends BotCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);
    private static final String LOGTAG = "STARTCOMMAND";

    private CardService cardService;

    private SessionManager sessionManager;

    public StartCommand(final CardService cardService, final SessionManager sessionManager) {
        super("start", "With this command you can start the Bot");

        this.cardService = cardService;
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        UserSession userSession = new UserSession(chat.getId(), user.getUserName());

        Card card = cardService.getRandomCard();

        sessionManager.setCardId(userSession, card.getId());

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        if (card != null) {
            answer.setText(card.getWord());
        } else {
            answer.setText("No word for you :(");
        }

        try {
            absSender.execute(answer);
        } catch (TelegramApiException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
