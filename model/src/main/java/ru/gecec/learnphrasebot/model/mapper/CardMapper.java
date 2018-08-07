package ru.gecec.learnphrasebot.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.gecec.learnphrasebot.model.entity.Card;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardMapper implements RowMapper<Card> {
    @Override
    public Card mapRow(ResultSet resultSet, int i) throws SQLException {
        Card card = new Card();
        return card;
    }
}
