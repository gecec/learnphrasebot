package ru.gecec.learnphrasebot.model.entity;

import java.util.UUID;

public class Card {
    private UUID id;
    private String word;
    private String translation;
    private String category;
    private String subject; //Часть речи
    private String description;
    private int order;
}
