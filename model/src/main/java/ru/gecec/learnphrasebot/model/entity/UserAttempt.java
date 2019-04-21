package ru.gecec.learnphrasebot.model.entity;

import java.util.Date;

public class UserAttempt {
    private String id;
    private int userId;
    private String cardId;
    private int wordSuccess;
    private int wordFailure;
    private int translationSuccess;
    private int translationFailure;
    private Date lastShowDate;

    public UserAttempt() {
    }

    public UserAttempt(int userId, String cardId) {
        this.userId = userId;
        this.cardId = cardId;

        this.wordSuccess = 0;
        this.wordFailure = 0;
        this.translationSuccess = 0;
        this.translationFailure = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getWordSuccess() {
        return wordSuccess;
    }

    public void setWordSuccess(int wordSuccess) {
        this.wordSuccess = wordSuccess;
    }

    public int getWordFailure() {
        return wordFailure;
    }

    public void setWordFailure(int wordFailure) {
        this.wordFailure = wordFailure;
    }

    public int getTranslationSuccess() {
        return translationSuccess;
    }

    public void setTranslationSuccess(int translationSuccess) {
        this.translationSuccess = translationSuccess;
    }

    public int getTranslationFailure() {
        return translationFailure;
    }

    public void setTranslationFailure(int translationFailure) {
        this.translationFailure = translationFailure;
    }

    public Date getLastShowDate() {
        return lastShowDate;
    }

    public void setLastShowDate(Date lastShowDate) {
        this.lastShowDate = lastShowDate;
    }
}
