package ru.gecec.learnphrasebot.bot.commands;


import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

import java.util.Arrays;
import java.util.List;

public class CreateCardCommand extends BotCommand implements BasicCommand {

    private final static List<String> admins = Arrays.asList("gecec");
    private static final String LOGTAG = "CREATECARDCOMMAND";

    private CardRepository cardRepository;

    public CreateCardCommand(final CardRepository cardRepository) {
        super("create", "With this command you can create new word card");
        this.cardRepository = cardRepository;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        /*if (!admins.stream().filter(admin -> user.getUserName().equals(admin)).findFirst().isPresent()) {
            BotLogger.error(LOGTAG, String.format("Unauthorized user %s tries to create card", user.getUserName()));
            sendMessage(chat.getId().toString(), absSender, String.format("У вас нет прав на создание карточки"));
            return;
        }*/

        if (strings.length != 0) {
            String[] words = String.join("", strings).split(":");
            if (words.length != 2) {
                sendMessage(chat.getId().toString(), absSender, String.format("Неверный формат. Правильно <перевод>:<слово или фраза на иврите>"));
            }

            Card card = new Card();
            card.setWord(words[1]);
            card.setWordTranslation(words[0]);
            card = cardRepository.save(card);
            sendMessage(chat.getId().toString(), absSender, String.format("Успешно создана карточка %s", card.toString()));
        } else {
            sendMessage(chat.getId().toString(), absSender, String.format("Неверный формат. Правильно <перевод>:<слово или фраза на иврите>"));
            return;
        }
    }


    @Override
    public String getLogtag() {
        return LOGTAG;
    }
}
