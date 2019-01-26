package ru.gecec.learnphrasebot.bot.commands;


import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.gecec.learnphrasebot.bot.BotMode;
import ru.gecec.learnphrasebot.bot.session.SessionBean;

public class ModeCommand extends BotCommand implements BasicCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(ModeCommand.class);
    private static final String LOGTAG = "MODECOMMAND";

    private SessionBean sessionManager;

    public ModeCommand(SessionBean sessionManager) {
        super("mode", "With this command you set language mode: ALL, HEBREW, RUSSIAN");
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        Session session = sessionManager.getSession(chat.getId(), user.getUserName());

        if (strings.length != 0){

        }

        BotMode mode = (BotMode) session.getAttribute("mode");

        String currentStatus = "HEBREW";
        sendMessage(chat.getId().toString(), absSender, currentStatus);
    }

    @Override
    public String getLogtag() {
        return LOGTAG;
    }
}
