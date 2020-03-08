package ru.gecec.learnphrasebot.bot.commands;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.UserSession;

public class InfoCommand extends BotCommand implements BasicCommand {
    private final static Logger log = LoggerFactory.getLogger(InfoCommand.class);

    private SessionManager sessionManager;

    public InfoCommand(final SessionManager sessionManager) {
        super("info", "With this command you get info about current mode");
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            UserSession session = new UserSession(chat.getId(), user.getUserName());
            String currentStatus = String.format("Текущий языковой режим: %s", sessionManager.getMode(session));
            sendMessage(chat.getId().toString(), absSender, currentStatus);
        } catch (TelegramApiException ex){
            log.error(ex.getMessage(), ex);
        }
    }
}
