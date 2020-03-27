package ru.gecec.learnphrasebot.model.entity;

import java.util.Objects;
import java.util.StringJoiner;

public class Card {
    private String id;
    private String word;
    private String translation;
    private String category;
    private String subject; //Часть речи
    private String description;
    private String transcription;

    private int wordOrder;

    public Card() {
    }

    public Card(String id, String word, String translation) {
        this.id = id;
        this.word = word;
        this.translation = translation;
    }

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

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
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

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Card.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("word='" + word + "'")
                .add("translation='" + translation + "'")
                .add("category='" + category + "'")
                .add("subject='" + subject + "'")
                .add("description='" + description + "'")
                .add("transcription='" + transcription + "'")
                .add("wordOrder=" + wordOrder)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return wordOrder == card.wordOrder &&
                id.equals(card.id) &&
                word.equals(card.word) &&
                Objects.equals(translation, card.translation) &&
                Objects.equals(category, card.category) &&
                Objects.equals(subject, card.subject) &&
                Objects.equals(description, card.description) &&
                Objects.equals(transcription, card.transcription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, translation, category, subject, description, transcription, wordOrder);
    }
}
