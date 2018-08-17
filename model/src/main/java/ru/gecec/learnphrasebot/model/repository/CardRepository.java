package ru.gecec.learnphrasebot.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.logging.BotLogger;
import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.mapper.CardMapper;

import java.util.UUID;

@Repository
public class CardRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public Card getById(final String id){
        final String sql = "select id, word, word_translation, category, subject, description, word_order from card where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new CardMapper());
    }

    @Transactional(readOnly = true)
    public Card getByOrder(int order){
        final String sql = "select id, word, word_translation, category, subject, description, word_order from card where word_order = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{order}, new CardMapper());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Card save(final Card card) {
        if (StringUtils.isEmpty(card.getId())) {
            card.setId(UUID.randomUUID().toString());
            final String sql = "insert into card (id, word, word_translation, category, subject, description) values (?,?,?,?,?,?)";

            try {
                jdbcTemplate.update(sql, new Object[]{
                        card.getId(),
                        card.getWord(),
                        card.getWordTranslation(),
                        card.getCategory(),
                        card.getSubject(),
                        card.getDescription()
                });
            } catch (DataAccessException ex){
                BotLogger.error("REPO", ex);
            }
        } else {
            final String sql = "update card set word=?, word_translation=?, category=?, subject=?, description=?, wordOrder=? where id=?";
            throw new UnsupportedOperationException("update not implemented");
        }

        return getById(card.getId());
    }
}
