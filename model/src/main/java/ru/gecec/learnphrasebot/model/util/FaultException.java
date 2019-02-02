package ru.gecec.learnphrasebot.model.util;


public class FaultException extends RuntimeException {
    private String code;

    public FaultException() {
    }

    public FaultException(Throwable cause) {
        super(cause);
    }

    public FaultException(String message) {
        super(message);
    }

    public FaultException(String message, String code) {
        super(message);
        this.code = code;
    }

    public FaultException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public FaultException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause);
    }

    public String getCode() {
        return this.code;
    }
}
