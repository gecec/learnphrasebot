package ru.gecec.learnphrasebot.bot.service;

import ru.gecec.learnphrasebot.model.entity.Card;

import static ru.gecec.learnphrasebot.bot.service.BotMode.*;

public class WordChecker {

    private static final String CORRECT_ANSWER = "Правильно! :)";
    private static final String WRONG_ANSWER = "Неверно :( Правильный ответ: %s";
    private static final String WRONG_MODE = "Неверный языковой режим";

    public String check(Card card, BotMode mode, String answer){
        boolean result = false;

        if (HEBREW.equals(mode) || RANDOM_HEBREW.equals(mode)) return  checkHebrew(card, answer);

        if (RUSSIAN.equals(mode) || RANDOM_RUSSIAN.equals(mode)) return checkRussian(card, answer);

        return String.format(WRONG_MODE, mode);
    }

    private String checkHebrew(Card card, String answer){
        if (card.getWordTranslation().equalsIgnoreCase(answer)){
            return CORRECT_ANSWER;
        }

        return String.format(WRONG_ANSWER, card.getWordTranslation());
    }

    private String checkRussian(Card card, String answer){
        if (card.getWord().equalsIgnoreCase(answer)){
            return CORRECT_ANSWER;
        }

        return String.format(WRONG_ANSWER, card.getWord());
    }
}
