package ru.gecec.learnphrasebot.bot.session;

import org.apache.shiro.session.mgt.SessionContext;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import java.io.Serializable;
import java.util.HashMap;

public class DefaultChatSessionContext extends HashMap<String, Object> implements SessionContext {
    private long sessionId;
    private String host;

    public DefaultChatSessionContext(long sessionId, String host) {
        this.sessionId = sessionId;
        this.host = host;
    }

    public DefaultChatSessionContext(UserSession userSession) {
        this.sessionId = userSession.getChatId();
        this.host = userSession.getUserName();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public Serializable getSessionId() {
        return sessionId;
    }

    @Override
    public void setSessionId(Serializable serializable) {
        this.sessionId = (long) serializable;
    }
}
