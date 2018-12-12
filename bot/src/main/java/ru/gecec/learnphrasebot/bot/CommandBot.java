package ru.gecec.learnphrasebot.bot;

import org.apache.shiro.session.Session;
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
import org.telegram.telegrambots.meta.logging.BotLogger;
import ru.gecec.learnphrasebot.bot.commands.CreateCardCommand;
import ru.gecec.learnphrasebot.bot.commands.HelpCommand;
import ru.gecec.learnphrasebot.bot.commands.ListCardsCommand;
import ru.gecec.learnphrasebot.bot.commands.StartCommand;
import ru.gecec.learnphrasebot.bot.session.SessionBean;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

import javax.annotation.PostConstruct;

@Component
public class CommandBot extends TelegramLongPollingCommandBot {
    private static final String LOGTAG = "COMMANDSHANDLER";

    @Autowired
    private CardRepository cardRepository;

    @Value("${bot.token}")
    private String token;

    @Autowired
    SessionBean sessionBean;

    public CommandBot(@Autowired DefaultBotOptions options, @Value("${bot.username}") String username) {
        super(options, username);
    }

    @PostConstruct
    public void init() {
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        register(new StartCommand(cardRepository, sessionBean));
        register(new CreateCardCommand(cardRepository));
        register(new ListCardsCommand(cardRepository));

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help ");
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                //FIXME session is null
                Session session = sessionBean.getSession(message.getChatId(), message.getFrom().getUserName()).get();

                String cardId = (String) session.getAttribute("cardId");
                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(message.getChatId());

                if (!StringUtils.isEmpty(cardId)) {
                    Card card = cardRepository.getById(cardId);
                    if (card != null) {
                        if (card.getWordTranslation().equalsIgnoreCase(message.getText())) {
                            echoMessage.setText("Правильно! :)");
                        } else {
                            echoMessage.setText(String.format("Неверно :( Правильный ответ: %s", card.getWordTranslation()));
                        }
                    }
                } else {
                    echoMessage.setText("Для Вас не нашлось карточки, попробуйте в следующий раз :(");
                }

                try {
                    execute(echoMessage);

                    Card nextCard = cardRepository.getRandomCard();
                    session.setAttribute("cardId", nextCard.getId());
                    echoMessage.setText(nextCard.getWord());
                    execute(echoMessage);
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
