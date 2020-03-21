package ru.gecec.learnphrasebot.bot.commands.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gecec.learnphrasebot.model.entity.UserSession;

public interface Handler {

    @Autowired
    default void reg(SubCommandHandler subCommandHandler){
        subCommandHandler.register(this, getCode());
    }

    String handle(Message message, UserSession userSession);
    SubCommandEnum getCode();
}
