package ru.gecec.learnphrasebot.bot;

public class CardCreationException extends Exception {
    public CardCreationException() {
    }

    public CardCreationException(String message) {
        super(message);
    }

    public CardCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
