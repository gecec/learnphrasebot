package ru.gecec.learnphrasebot.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.gecec.learnphrasebot.model.util.SqlResolver;

public class BaseRepository {
    SqlResolver sqlResolver = new SqlResolver();

    @Autowired
    JdbcTemplate jdbcTemplate;
}
