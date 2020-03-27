package ru.gecec.learnphrasebot.model.entity;

public class CheckResult {
    private String result;
    private boolean isRight;

    public CheckResult(String result, boolean isRight) {
        this.result = result;
        this.isRight = isRight;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
