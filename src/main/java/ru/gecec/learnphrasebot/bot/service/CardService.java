package ru.gecec.learnphrasebot.bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.gecec.learnphrasebot.bot.CardCreationException;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserAttempt;
import ru.gecec.learnphrasebot.model.repository.CardRepository;
import ru.gecec.learnphrasebot.model.repository.UserAttemptsRepository;

import java.util.Optional;
import java.util.Random;

import static ru.gecec.learnphrasebot.bot.service.BotMode.HEBREW;

@Service
public class CardService {
    private final static Logger log = LoggerFactory.getLogger(CardService.class);

    private final static String TERM_SPLITTER = "@";

    private final CardRepository cardRepository;

    private final UserAttemptsRepository attemptsRepository;

    @Autowired
    public CardService(CardRepository cardRepository, UserAttemptsRepository attemptsRepository) {
        this.cardRepository = cardRepository;
        this.attemptsRepository = attemptsRepository;
    }

    private final Random random = new Random(System.currentTimeMillis());

    public Card getRandomCard() {
        int cardCount = cardRepository.getCardCount();
        int order = random.nextInt(cardCount);
        return cardRepository.getByOrder(order);
    }

    public Card getRarelyUsedCard(int userId, BotMode mode) {
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

    /**
     * Create card from string template: <term>@<translation>@<transcript>@<comment>
     *
     * @param template
     * @return
     * @throws CardCreationException
     */
    public Card createCardFromTemplate(final String template) throws CardCreationException {
        Assert.hasLength(template, "Card template must not be null");

        String[] terms = template.split(TERM_SPLITTER);

        if (terms.length < 2) {
            throw new CardCreationException(
                    String.format(
                            "String should fit templates:\n " +
                                    "<term>%s<translation>%s<transcript>%s<comment>,\n" +
                                    "<term>%s<translation>%s<transcript>,\n"+
                                    "<term>%s<translation>"
                            , TERM_SPLITTER
                            , TERM_SPLITTER
                            , TERM_SPLITTER
                            , TERM_SPLITTER
                            , TERM_SPLITTER
                            , TERM_SPLITTER));
        }

        try {
            Optional<Card> currentCard = cardRepository.getByWord(terms[0]);

            if (currentCard.isPresent()) {
                throw new CardCreationException(String.format("Card for word: %s already exists", terms[0]));
            }
        } catch (DataAccessException ex){
            log.warn("Card {} not exists", terms[0]);
        }

        Card card = new Card();
        card.setWord(terms[0]);
        card.setTranslation(terms[1]);

        if (terms.length > 2) card.setTranscription(terms[2]);
        if (terms.length > 3) card.setDescription(terms[3]);

        return cardRepository.save(card);
    }

    public Card create(final Card card) throws CardCreationException {
        try {
            Optional<Card> currentCard = cardRepository.getByWord(card.getWord());

            if (currentCard.isPresent()) {
                throw new CardCreationException(String.format("Card for word: %s already exists", card.getWord()));
            }
        } catch (DataAccessException ex){
            log.warn("Card {} not exists", card.getWord());
        }

        return cardRepository.save(card);
    }
}
