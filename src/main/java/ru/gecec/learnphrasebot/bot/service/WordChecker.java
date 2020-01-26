package ru.gecec.learnphrasebot.bot.service;

import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.CheckResult;

import static ru.gecec.learnphrasebot.bot.service.BotMode.*;

public class WordChecker {

    private static final String CORRECT_ANSWER = "Правильно! :)";
    private static final String WRONG_ANSWER = "Неверно :( Правильный ответ: %s";
    private static final String WRONG_MODE = "Неверный языковой режим";

    public CheckResult check(Card card, BotMode mode, String answer){
        if (HEBREW.equals(mode) || RANDOM_HEBREW.equals(mode)) return checkHebrew(card, answer);

        if (RUSSIAN.equals(mode) || RANDOM_RUSSIAN.equals(mode)) return checkRussian(card, answer);

        return new CheckResult(String.format(WRONG_MODE, mode), false);
    }

    private CheckResult checkHebrew(Card card, String answer){
        if (card.getTranslation().equalsIgnoreCase(answer)){
            return new CheckResult(CORRECT_ANSWER, true);
        }

        return new CheckResult(String.format(WRONG_ANSWER, card.getTranslation()), false);
    }

    private CheckResult checkRussian(Card card, String answer){
        if (card.getWord().equalsIgnoreCase(answer)){
            return new CheckResult(CORRECT_ANSWER, true);
        }

        return new CheckResult(String.format(WRONG_ANSWER, card.getWord()), false);
    }
}
