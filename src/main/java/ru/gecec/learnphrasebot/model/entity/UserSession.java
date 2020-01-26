package ru.gecec.learnphrasebot.model.entity;

public class UserSession {
    private Long chatId;
    private String userName;

    public UserSession(Long chatId, String userName) {
        this.chatId = chatId;
        this.userName = userName;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
