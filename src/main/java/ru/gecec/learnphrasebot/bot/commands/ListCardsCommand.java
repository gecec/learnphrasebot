package ru.gecec.learnphrasebot.bot.commands;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gecec.learnphrasebot.bot.service.SecurityService;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

public class ListCardsCommand extends BotCommand implements BasicCommand {
    private final static Logger log = LoggerFactory.getLogger(ListCardsCommand.class);

    private final CardRepository cardRepository;
    private final SecurityService securityService;

    public ListCardsCommand(final CardRepository cardRepository, final SecurityService securityService) {
        super("list", "With this command you list cards");
        this.cardRepository = cardRepository;
        this.securityService = securityService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            if (!securityService.isAdmin(user.getUserName())) {
                log.error(String.format("Unauthorized user %s tries to create card", user.getUserName()));
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
        } catch (TelegramApiException ex){
            log.error(ex.getMessage(), ex);
        }
    }
}
