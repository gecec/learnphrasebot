package ru.gecec.learnphrasebot.bot.commands.handler;

import liquibase.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserSession;

public class WordCommandHandler extends CommandHandler {
    private final SessionManager sessionManager;
    private final UserSession userSession;

    public WordCommandHandler(SessionManager sessionManager, UserSession userSession) {
        this.sessionManager = sessionManager;
        this.userSession = userSession;
    }

    @Override
    public String handle(Message message) {
        if (StringUtils.isEmpty(message.getText())){
            return "Слово не может быть пустым";
        }

        Card card = new Card();
        card.setWord(message.getText());
        sessionManager.setNewCard(userSession, card);
        sessionManager.setCommand(userSession, CreateCommandEnum.TRANSLATION);
        return "Перевод:";
    }
}
