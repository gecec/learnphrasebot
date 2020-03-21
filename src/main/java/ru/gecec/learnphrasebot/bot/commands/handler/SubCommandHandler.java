package ru.gecec.learnphrasebot.bot.commands.handler;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import java.util.HashMap;
import java.util.Map;

@Service
public class SubCommandHandler {
    private Map<SubCommandEnum, Handler> handlers = new HashMap<>();

    public void register(Handler handler, SubCommandEnum code){
        handlers.put(code, handler);
    }

    public String handle(final Message message, final SubCommandEnum type, final UserSession userSession){
        return handlers.get(type).handle(message, userSession);
    }
}
