package ru.gecec.learnphrasebot.bot.commands.handler.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gecec.learnphrasebot.bot.CardCreationException;
import ru.gecec.learnphrasebot.bot.commands.handler.Handler;
import ru.gecec.learnphrasebot.bot.commands.handler.SubCommandEnum;
import ru.gecec.learnphrasebot.bot.service.CardService;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import static java.lang.String.format;
import static ru.gecec.learnphrasebot.bot.commands.handler.SubCommandEnum.NONE;
import static ru.gecec.learnphrasebot.bot.commands.handler.SubCommandEnum.TRANSCRIPTION;

@Component
public class TranscriptionSubCommandHandler implements Handler {
    private final SessionManager sessionManager;
    private final CardService cardService;

    @Autowired
    public TranscriptionSubCommandHandler(SessionManager sessionManager, CardService cardService) {
        this.sessionManager = sessionManager;
        this.cardService = cardService;
    }

    @Override
    public String handle(Message message, UserSession userSession) {
        Card card = sessionManager.getNewCard(userSession);
        card.setTranscription(message.getText());

        try {
            Card storedCard = cardService.create(card);
            sessionManager.setCommand(userSession, NONE);
            return format("Карточка %s успешно создана", storedCard.toString());
        } catch (CardCreationException ex) {
            return format("Ошибка при создании карточки: %s", ex.getMessage());
        }
    }

    @Override
    public SubCommandEnum getCode() {
        return TRANSCRIPTION;
    }
}
