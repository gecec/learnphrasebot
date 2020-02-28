package ru.gecec.learnphrasebot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageUtil {
    private final MessageSource messageSource;

    @Autowired
    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String resolveTemplate(String key, Object ...args) {
        return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }
}
