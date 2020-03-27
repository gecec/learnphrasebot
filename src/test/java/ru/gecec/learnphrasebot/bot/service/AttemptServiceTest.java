package ru.gecec.learnphrasebot.bot.service;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.util.StringUtils;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.CheckResult;
import ru.gecec.learnphrasebot.model.entity.UserAttempt;
import ru.gecec.learnphrasebot.model.repository.UserAttemptsRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class AttemptServiceTest {

//    @Mock
    private UserAttemptsRepository attemptsRepository;

    private Map<String, Card> cardStorage;
    private Map<String, UserAttempt> attemptStorage;

//    @Before
    public void setUp() {
        initMocks(this);
        cardStorage = new HashMap<String, Card>() {{
            put("1", new Card("1", "aaa", "bbb"));
            put("2", new Card("2", "ccc", "ddd"));
            put("3", new Card("3", "eee", "fff"));
        }};

        attemptStorage = new HashMap<>();
    }

//    @Test
    public void processResult() throws Exception {
        when(attemptsRepository.getByUserIdAndCardId(anyInt(), anyString())).thenAnswer(
                (Answer<UserAttempt>) invocation -> {
                    String cardId = invocation.getArgument(1);
                    return attemptStorage.get(cardId);
                }
        );

        when(attemptsRepository.save(any(UserAttempt.class))).thenAnswer(
                (Answer<UserAttempt>) invocation -> {
                    UserAttempt attempt = (UserAttempt) invocation.getArgument(0);
                    if (StringUtils.isEmpty(attempt.getId())){
                        attempt.setId(UUID.randomUUID().toString());
                    }

                    attempt.setLastShowDate(new Date());
                    attemptStorage.put(attempt.getCardId(), attempt);
                    return attempt;
                }
        );

        AttemptService service = new AttemptService(attemptsRepository);
        CheckResult checkResult = new CheckResult("Correct", true);
        service.processResult(checkResult, "1", 1, BotMode.HEBREW);

    }
}