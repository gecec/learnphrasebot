package ru.gecec.learnphrasebot.model.entity;

public class CheckResult {
    private String answer;
    private boolean isRight;

    public CheckResult(String answer, boolean isRight) {
        this.answer = answer;
        this.isRight = isRight;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
