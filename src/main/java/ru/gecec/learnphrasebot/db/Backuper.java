package ru.gecec.learnphrasebot.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.logging.BotLogger;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Backuper {
    private static final String LOGTAG = "BACKUP";

    private final static String BACKUP_FILENAME = "_bak.sql";

    @Value("#{environment.backup_path}")
    private String backupPath;

    @Autowired
    private CardRepository repository;

    @PostConstruct
    public void init() {
        BotLogger.info(LOGTAG, "Backuper init");
    }

    @Scheduled(fixedRate = 60000)
    public void backup(){
        BotLogger.info(LOGTAG, "Start backuping DB...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_YY_HH_mm_SS");

        String filename = String.format("%s/%s%s", backupPath, dateFormat.format(new Date()), BACKUP_FILENAME);
        repository.backup(filename);

        BotLogger.info(LOGTAG, String.format("DB stored to %s", filename));
    }
}
