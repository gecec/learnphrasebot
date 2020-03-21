package ru.gecec.learnphrasebot.bot.commands.handler.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gecec.learnphrasebot.bot.commands.handler.Handler;
import ru.gecec.learnphrasebot.bot.commands.handler.SubCommandEnum;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import static ru.gecec.learnphrasebot.bot.commands.handler.SubCommandEnum.TRANSLATION;

@Service
public class TranslationSubCommandHandler implements Handler {
    private final SessionManager sessionManager;


    @Autowired
    public TranslationSubCommandHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public String handle(final Message message, final UserSession userSession) {
        if (StringUtils.isEmpty(message.getText())){
            return "Перевод не может быть пустым";
        }

        Card card = sessionManager.getNewCard(userSession);
        card.setTranslation(message.getText());
        sessionManager.setCommand(userSession, SubCommandEnum.TRANSCRIPTION);
        return "Транскрипция:";
    }

    @Override
    public SubCommandEnum getCode() {
        return TRANSLATION;
    }
}
