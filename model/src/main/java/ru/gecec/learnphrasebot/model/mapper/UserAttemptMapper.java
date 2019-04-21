package ru.gecec.learnphrasebot.model.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.gecec.learnphrasebot.model.entity.UserAttempt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAttemptMapper implements RowMapper<UserAttempt> {
    @Override
    public UserAttempt mapRow(ResultSet resultSet, int i) throws SQLException {
        UserAttempt attempt = new UserAttempt();

        attempt.setId(resultSet.getString("id"));
        attempt.setUserId(resultSet.getInt("user_id"));
        attempt.setCardId(resultSet.getString("card_id"));
        attempt.setWordSuccess(resultSet.getInt("word_success"));
        attempt.setWordFailure(resultSet.getInt("word_fail"));
        attempt.setTranslationSuccess(resultSet.getInt("translation_success"));
        attempt.setTranslationFailure(resultSet.getInt("translation_failure"));
        attempt.setLastShowDate(resultSet.getTimestamp("last_showtime"));

        return attempt;
    }
}
