package ru.gecec.learnphrasebot.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.gecec.learnphrasebot.model.util.SqlResolver;

public abstract class BaseRepository {
    @Autowired
    protected SqlResolver sqlResolver;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected NamedParameterJdbcTemplate namedJdbcTemplate;

    protected String queryName(String name){
        return String.format("%s.%s", getQueryPrefix(), name);
    }

    protected abstract String getQueryPrefix();
}
