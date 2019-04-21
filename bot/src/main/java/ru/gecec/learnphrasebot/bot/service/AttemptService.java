package ru.gecec.learnphrasebot.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gecec.learnphrasebot.model.entity.CheckResult;
import ru.gecec.learnphrasebot.model.entity.UserAttempt;
import ru.gecec.learnphrasebot.model.repository.UserAttemptsRepository;

import java.util.Optional;

import static ru.gecec.learnphrasebot.bot.service.BotMode.*;

@Service
public class AttemptService {
    @Autowired
    private UserAttemptsRepository attemptsRepository;

    public void processResult(CheckResult result, String cardId, int userId, BotMode mode){
        Optional<UserAttempt> res = attemptsRepository.getByUserIdAndCardId(userId, cardId);

        UserAttempt attempt;
        if (!res.isPresent()) {
            attempt = new UserAttempt(userId, cardId);
        } else {
            attempt = res.get();
        }

        if (result.isRight()){
            if (HEBREW.equals(mode)) wordSuccess(attempt);
            if (RUSSIAN.equals(mode)) translationSuccess(attempt);
        } else {
            if (HEBREW.equals(mode)) wordFailure(attempt);
            if (RUSSIAN.equals(mode)) translationFailure(attempt);
        }

        attemptsRepository.save(attempt);
    }

    private void wordSuccess(UserAttempt attempt){
        int current = attempt.getWordSuccess();
        attempt.setWordSuccess(++current);
    }

    private void wordFailure(UserAttempt attempt){
        int current = attempt.getWordFailure();
        attempt.setWordFailure(++current);
    }

    private void translationSuccess(UserAttempt attempt){
        int current = attempt.getTranslationSuccess();
        attempt.setTranslationSuccess(++current);
    }

    private void translationFailure(UserAttempt attempt){
        int current = attempt.getTranslationFailure();
        attempt.setTranslationFailure(++current);
    }
}
