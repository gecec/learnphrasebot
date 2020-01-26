package ru.gecec.learnphrasebot.bot.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.stereotype.Component;
import ru.gecec.learnphrasebot.bot.service.BotMode;
import ru.gecec.learnphrasebot.model.entity.UserSession;

import static ru.gecec.learnphrasebot.bot.service.BotMode.*;
import static ru.gecec.learnphrasebot.bot.session.SessionAttributeCode.*;

@Component
public class SessionManager {
    private DefaultSessionManager sessionManager;

    public SessionManager() {
        this.sessionManager = new DefaultSessionManager();
    }

    public Session getSession(UserSession userSession) {
        DefaultChatIdConverter chatIdConverter = new DefaultChatIdConverter(userSession.getChatId());

        try {
            return sessionManager.getSession(chatIdConverter);
        } catch (UnknownSessionException ex) {
            SessionContext botSession = new DefaultChatSessionContext(userSession);
            AbstractSessionDAO sessionDAO = (AbstractSessionDAO) sessionManager.getSessionDAO();
            sessionDAO.setSessionIdGenerator(chatIdConverter);
            Session session =  sessionManager.start(botSession);
            session.setAttribute(MODE, HEBREW);
            return session;
        }
    }

    public BotMode getMode(UserSession userSession){
        Session session = getSession(userSession);
        return (BotMode) session.getAttribute(MODE);
    }

    public String getCardId(UserSession userSession){
        Session session = getSession(userSession);
        return (String) session.getAttribute(CARD_ID);
    }

    public void setCardId(UserSession userSession, String cardId){
        Session session = getSession(userSession);
        session.setAttribute(CARD_ID, cardId);
    }

    public void setMode(UserSession userSession, BotMode mode){
        Session session = getSession(userSession);
        session.setAttribute(MODE, mode);
    }

    public void setRandomMode(UserSession userSession, BotMode mode){
        Session session = getSession(userSession);
        session.setAttribute(RANDOM_MODE, mode);
    }

    public BotMode getRandomMode(UserSession userSession){
        Session session = getSession(userSession);
        return (BotMode) session.getAttribute(RANDOM_MODE);
    }

    public void invertRandomMode(UserSession userSession){
        BotMode currentRandomMode = getRandomMode(userSession);

        if (RANDOM_HEBREW.equals(currentRandomMode)) {
            setRandomMode(userSession, RANDOM_RUSSIAN);
        } else {
            setRandomMode(userSession, RANDOM_HEBREW);
        }
    }

    public DefaultSessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(DefaultSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
}
