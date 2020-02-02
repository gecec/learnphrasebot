package ru.gecec.learnphrasebot.bot.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import ru.gecec.learnphrasebot.bot.CardCreationException;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.repository.CardRepository;
import ru.gecec.learnphrasebot.model.repository.UserAttemptsRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserAttemptsRepository attemptsRepository;

    private ru.gecec.learnphrasebot.bot.service.CardService cardService;

    @Before
    public void init() {
        initMocks(this);
        cardService = new CardService(cardRepository, attemptsRepository);
    }

    @Test
    public void shouldCreateCardFromTemplate() throws Exception {
        when(cardRepository.save(any(Card.class))).thenAnswer(
                (Answer<Card>) invocation -> {
                    Card card = (Card) invocation.getArguments()[0];
                    card.setId("1");
                    return card;
                }
        );

        Card expected1 = new Card();
        expected1.setId("1");
        expected1.setWord("test");
        expected1.setTranslation("test");
        expected1.setTranscription("test");
        expected1.setDescription("test");

        assertEquals(expected1, cardService.createCardFromTemplate("test@test@test@test"));
        assertEquals(expected1, cardService.createCardFromTemplate("test@test@test@test@"));
        assertEquals(expected1, cardService.createCardFromTemplate("test@test@test@test@test"));

        Card expected2 = new Card();
        expected2.setId("1");
        expected2.setWord("test");
        expected2.setTranslation("test");

        assertEquals(expected2, cardService.createCardFromTemplate("test@test@@"));
        assertEquals(expected2, cardService.createCardFromTemplate("test@test"));

    }

    @Test(expected = CardCreationException.class)
    public void shoudFailWhenLessThanTwoTerms() throws Exception {
        cardService.createCardFromTemplate("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenEmptyTemplate() throws Exception {
        cardService.createCardFromTemplate("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenNullTemplate() throws Exception {
        cardService.createCardFromTemplate(null);
    }
}
