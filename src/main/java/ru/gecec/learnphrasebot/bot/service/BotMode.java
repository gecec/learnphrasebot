package ru.gecec.learnphrasebot.bot.service;

public enum BotMode {
    HEBREW("hebrew"),
    RUSSIAN("russian"),

    RANDOM("random"), //first card in hebrew, next - in russian
    RANDOM_RUSSIAN("random_russian"),
    RANDOM_HEBREW("random_hebrew");

    private String value;

    BotMode(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
