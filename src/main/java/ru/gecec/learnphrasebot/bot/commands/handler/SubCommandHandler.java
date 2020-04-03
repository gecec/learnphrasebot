package ru.gecec.learnphrasebot.bot.commands.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SubCommandHandler {
    private final static Logger log = LoggerFactory.getLogger(SubCommandHandler.class);

    private Map<SubCommandEnum, Handler> handlers = new HashMap<>();

    public void register(Handler handler, SubCommandEnum code){
        handlers.put(code, handler);
    }

    public String handle(final Message message, final SubCommandEnum type, final UserSession userSession){
        Handler handler = handlers.get(type);
        if (Objects.isNull(handler)){
            log.error(String.format("Handler for comand [%s] not found", type));
            return "Произошла ошибка";
        }

        return handlers.get(type).handle(message, userSession);
    }
}
