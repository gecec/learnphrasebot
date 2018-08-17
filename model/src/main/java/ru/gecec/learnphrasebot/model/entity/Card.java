package ru.gecec.learnphrasebot.model.entity;

public class Card {
    private String id;
    private String word;
    private String wordTranslation;
    private String category;
    private String subject; //Часть речи
    private String description;
    private int wordOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordTranslation() {
        return wordTranslation;
    }

    public void setWordTranslation(String wordTranslation) {
        this.wordTranslation = wordTranslation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWordOrder() {
        return wordOrder;
    }

    public void setWordOrder(int wordOrder) {
        this.wordOrder = wordOrder;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                ", wordTranslation='" + wordTranslation + '\'' +
                ", category='" + category + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", wordOrder=" + wordOrder +
                '}';
    }
}
