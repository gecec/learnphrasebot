package ru.gecec.learnphrasebot.bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

import java.util.Random;

@Service
public class CardService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardRepository cardRepository;

    private final Random random = new Random(System.currentTimeMillis());

    public Card getRandomCard(){
        int cardCount = cardRepository.getCardCount();
        int order = random.nextInt(cardCount);
        return cardRepository.getByOrder(order);
    }
}
