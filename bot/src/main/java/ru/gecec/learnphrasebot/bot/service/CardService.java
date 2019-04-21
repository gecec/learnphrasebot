package ru.gecec.learnphrasebot.bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserAttempt;
import ru.gecec.learnphrasebot.model.repository.CardRepository;
import ru.gecec.learnphrasebot.model.repository.UserAttemptsRepository;

import java.util.Optional;
import java.util.Random;

import static ru.gecec.learnphrasebot.bot.service.BotMode.HEBREW;

@Service
public class CardService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserAttemptsRepository attemptsRepository;

    private final Random random = new Random(System.currentTimeMillis());

    public Card getRandomCard(){
        int cardCount = cardRepository.getCardCount();
        int order = random.nextInt(cardCount);
        return cardRepository.getByOrder(order);
    }

    public Card getRarelyUsedCard(int userId, BotMode mode){
        if (mode == null) mode = HEBREW;

        Optional<Card> result = cardRepository.getNotAttemptedByUser(userId);

        if (result.isPresent()) return result.get();

        Optional<UserAttempt> attempt;

        if (HEBREW.equals(mode)) {
            attempt = attemptsRepository.getWorstWord(userId);
        } else {
            attempt = attemptsRepository.getWorstTranslation(userId);
        }

        return attempt
                .map(att -> cardRepository.getById(att.getCardId()))
                .orElse(getRandomCard());
    }
}
