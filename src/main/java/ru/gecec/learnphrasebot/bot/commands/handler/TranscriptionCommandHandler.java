package ru.gecec.learnphrasebot.bot.commands.handler;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gecec.learnphrasebot.bot.CardCreationException;
import ru.gecec.learnphrasebot.bot.service.CardService;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import static java.lang.String.format;
import static ru.gecec.learnphrasebot.bot.commands.handler.CreateCommandEnum.NONE;

public class TranscriptionCommandHandler extends CommandHandler {
    private final SessionManager sessionManager;
    private final UserSession userSession;
    private final CardService cardService;

    public TranscriptionCommandHandler(SessionManager sessionManager, UserSession userSession, CardService cardService) {
        this.sessionManager = sessionManager;
        this.userSession = userSession;
        this.cardService = cardService;
    }

    @Override
    public String handle(Message message) {
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
}
