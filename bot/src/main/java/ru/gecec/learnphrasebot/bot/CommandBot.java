package ru.gecec.learnphrasebot.bot;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gecec.learnphrasebot.bot.commands.CreateCardCommand;
import ru.gecec.learnphrasebot.bot.commands.HelpCommand;
import ru.gecec.learnphrasebot.bot.commands.InfoCommand;
import ru.gecec.learnphrasebot.bot.commands.ListCardsCommand;
import ru.gecec.learnphrasebot.bot.commands.ModeCommand;
import ru.gecec.learnphrasebot.bot.commands.StartCommand;
import ru.gecec.learnphrasebot.bot.service.BotMode;
import ru.gecec.learnphrasebot.bot.service.CardService;
import ru.gecec.learnphrasebot.bot.service.WordChecker;
import ru.gecec.learnphrasebot.bot.session.SessionManager;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.entity.UserSession;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

import javax.annotation.PostConstruct;

import static ru.gecec.learnphrasebot.bot.service.BotMode.*;

@Component
public class CommandBot extends TelegramLongPollingCommandBot {
    private final static Logger LOGGER = LoggerFactory.getLogger(CommandBot.class);

    private static final String LOGTAG = "COMMANDSHANDLER";

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    private WordChecker checker;

    @Value("${bot.token}")
    private String token;

    @Autowired
    SessionManager sessionManager;

    public CommandBot(@Autowired DefaultBotOptions options, @Value("${bot.username}") String username) {
        super(options, username);
        checker = new WordChecker();
    }

    @PostConstruct
    public void init() {
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        register(new StartCommand(cardService, sessionManager));
        register(new CreateCardCommand(cardRepository));
        register(new ListCardsCommand(cardRepository));
        register(new InfoCommand(sessionManager));
        register(new ModeCommand(sessionManager));

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help ");
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException ex) {
                LOGGER.error(ex.getMessage(), ex);
                throw new IllegalStateException(ex);
            }

            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                LOGGER.info(message.getText());

                UserSession userSession = new UserSession(message.getChatId(), message.getFrom().getUserName());

                String cardId = sessionManager.getCardId(userSession);
                BotMode currentMode = sessionManager.getMode(userSession);

                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(message.getChatId());

                if (!StringUtils.isEmpty(cardId)) {
                    Card card = cardRepository.getById(cardId);
                    if (card != null) {
                        if (RANDOM.equals(currentMode)){
                            BotMode randomMode = sessionManager.getRandomMode(userSession);
                            echoMessage.setText(checker.check(card, randomMode, message.getText()));
                            sessionManager.invertRandomMode(userSession);
                        } else {
                            echoMessage.setText(checker.check(card, currentMode, message.getText()));
                        }
                    }
                } else {
                    echoMessage.setText("Для Вас не нашлось карточки, попробуйте в следующий раз :(");
                }

                try {
                    execute(echoMessage);

                    Card nextCard = cardService.getRandomCard();

                    sessionManager.setCardId(userSession, nextCard.getId());

                    if (currentMode == null) sessionManager.setMode(userSession, HEBREW);

                    echoMessage.setText(getWord(userSession, currentMode, nextCard));

                    execute(echoMessage);
                } catch (TelegramApiException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    private String getWord(UserSession userSession, BotMode currentMode, Card nextCard) {
        if (HEBREW.equals(currentMode)) return nextCard.getWord();

        if (RUSSIAN.equals(currentMode)) return nextCard.getWordTranslation();

        if (RANDOM.equals(currentMode)){
            BotMode randomMode = sessionManager.getRandomMode(userSession);
            if (RANDOM_HEBREW.equals(randomMode)){
                return nextCard.getWord();
            } else if (RANDOM_RUSSIAN.equals(randomMode)){
                return nextCard.getWordTranslation();
            }
        }

        return "";
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
