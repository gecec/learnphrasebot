package ru.gecec.learnphrasebot.bot.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionBean {
    private DefaultSessionManager sessionManager;

    public SessionBean() {
        this.sessionManager = new DefaultSessionManager();
    }

    public Optional<Session> getSession(final Long chatId, String fromUserName) {
        DefaultChatIdConverter chatIdConverter = new DefaultChatIdConverter(chatId);

        try {
            return Optional.of(sessionManager.getSession(chatIdConverter));
        } catch (UnknownSessionException ex) {
            SessionContext botSession = new DefaultChatSessionContext(chatId, fromUserName);
            AbstractSessionDAO sessionDAO = (AbstractSessionDAO) sessionManager.getSessionDAO();
            sessionDAO.setSessionIdGenerator(chatIdConverter);
            return Optional.of(sessionManager.start(botSession));
        }
    }

    public DefaultSessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(DefaultSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
}
