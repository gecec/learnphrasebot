package ru.gecec.learnphrasebot.bot.commands.handler.create;

import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gecec.learnphrasebot.bot.commands.handler.Handler;
import ru.gecec.learnphrasebot.bot.commands.handler.SubCommandEnum;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import static ru.gecec.learnphrasebot.bot.commands.handler.SubCommandEnum.WORD;

@Component
public class WordSubCommandHandler implements Handler {
    private final SessionManager sessionManager;

    @Autowired
    public WordSubCommandHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public String handle(final Message message, final UserSession userSession) {
        if (StringUtils.isEmpty(message.getText())){
            return "Слово не может быть пустым";
        }

        Card card =  new Card();
        card.setWord(message.getText());
        sessionManager.setNewCard(userSession, card);
        sessionManager.setCommand(userSession, SubCommandEnum.TRANSLATION);
        return "Перевод:";
    }

    @Override
    public SubCommandEnum getCode() {
        return WORD;
    }
}
