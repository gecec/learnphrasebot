package ru.gecec.learnphrasebot.bot.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SecurityService {
    private final static List<String> admins = Arrays.asList("gecec", "Ksuha_muha");

    public boolean isAdmin(final String userName) {
        if (!admins
                .stream()
                .anyMatch(admin -> userName.equals(admin))
        ) return false;

        return true;
    }
}
