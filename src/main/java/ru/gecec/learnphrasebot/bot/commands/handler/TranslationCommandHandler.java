package ru.gecec.learnphrasebot.bot.commands.handler;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserSession;

public class TranslationCommandHandler extends CommandHandler {
    private final SessionManager sessionManager;
    private final UserSession userSession;

    public TranslationCommandHandler(SessionManager sessionManager, UserSession userSession) {
        this.sessionManager = sessionManager;
        this.userSession = userSession;
    }

    @Override
    public String handle(Message message) {
        if (StringUtils.isEmpty(message.getText())){
            return "Перевод не может быть пустым";
        }

        Card card = sessionManager.getNewCard(userSession);
        card.setTranslation(message.getText());
        sessionManager.setCommand(userSession, CreateCommandEnum.TRANSCRIPTION);
        return "Транскрипция:";
    }
}
