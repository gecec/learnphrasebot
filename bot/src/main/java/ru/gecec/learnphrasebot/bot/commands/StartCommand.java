package ru.gecec.learnphrasebot.bot.commands;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import ru.gecec.learnphrasebot.bot.session.SessionBean;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

import java.util.Optional;

public class StartCommand extends BotCommand {
    private static final String LOGTAG = "STARTCOMMAND";

    private CardRepository cardRepository;

    private SessionBean sessionManager;

    public StartCommand(final CardRepository cardRepository, final SessionBean sessionBean) {
        super("start", "With this command you can start the Bot");

        this.cardRepository = cardRepository;
        this.sessionManager = sessionBean;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        Session session = sessionManager.getSession(chat.getId(), user.getUserName()).get();

        String text = String.format("Welcome %s %s ! This bot will learn you hebrew words.", user.getFirstName(), user.getLastName());
        Card card = cardRepository.getByOrder(new java.util.Random().nextInt(10));

        session.setAttribute("cardId", card.getId());

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        if (card != null) {
            answer.setText(card.getWord());
        } else {
            answer.setText("No word for you :(");
        }

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
