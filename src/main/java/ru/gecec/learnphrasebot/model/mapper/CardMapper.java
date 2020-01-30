package ru.gecec.learnphrasebot.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.gecec.learnphrasebot.model.entity.Card;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardMapper implements RowMapper<Card> {
    @Override
    public Card mapRow(ResultSet resultSet, int i) throws SQLException {
        Card card = new Card();
        card.setId(resultSet.getString("id"));
        card.setWord(resultSet.getString("word"));
        card.setTranslation(resultSet.getString("word_translation"));
        card.setCategory(resultSet.getString("category"));
        card.setSubject(resultSet.getString("subject"));
        card.setDescription(resultSet.getString("description"));
        card.setWordOrder(resultSet.getInt("word_order"));
        card.setTranscription(resultSet.getString("transcription"));

        return card;
    }
}
