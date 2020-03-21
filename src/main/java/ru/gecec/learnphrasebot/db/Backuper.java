package ru.gecec.learnphrasebot.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.gecec.learnphrasebot.model.repository.CardRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Backuper {
    private final static Logger log = LoggerFactory.getLogger(Backuper.class);

    private final static String BACKUP_FILENAME = "_bak.sql";

    @Value("#{environment.backup_path}")
    private String backupPath;

    @Autowired
    private CardRepository repository;

    @Scheduled(fixedRate = 43200000, initialDelay = 300000) //millisec
    public void backup(){
        if (StringUtils.isEmpty(backupPath)){
            log.warn("Backup path is empty, unable to create backup");
        }

        log.info("Start backuping DB...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_YY_HH_mm_SS");

        String filename = String.format("%s/%s%s", backupPath, dateFormat.format(new Date()), BACKUP_FILENAME);
        repository.backup(filename);

        log.info(String.format("DB stored to %s", filename));
    }
}
