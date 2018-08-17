package ru.gecec.learnphrasebot.bot.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

public class CreateCardCommand extends BotCommand {
    private static final String LOGTAG = "CREATECARDCOMMAND";

    private CardRepository cardRepository;

    public CreateCardCommand(final CardRepository cardRepository) {
        super("create", "With this command you can create new word card");
        this.cardRepository = cardRepository;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String text = String.format("Welcome %s %s ! This bot will learn you hebrew words.", user.getFirstName(), user.getLastName());

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(text);

        if (strings.length == 2){
            Card card = new Card();
            card.setWord(strings[0]);
            card.setWordTranslation(strings[1]);
            card = cardRepository.save(card);
            answer.setText(String.format("Успешно создана карточка %s", card.toString()));
        }

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
