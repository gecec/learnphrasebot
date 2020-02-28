package ru.gecec.learnphrasebot.model.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ru.gecec.learnphrasebot.model.entity.Card;
import ru.gecec.learnphrasebot.model.mapper.CardMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CardRepository extends BaseRepository {

    private final static Logger log = LoggerFactory.getLogger(CardRepository.class);

    @Transactional(readOnly = true)
    public Card getById(final String id) {
        final String sql = sqlResolver.getSql(queryName("getById"));
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new CardMapper());
    }

    @Transactional(readOnly = true)
    public Optional<Card> getByWord(final String word) {
        final String sql = sqlResolver.getSql(queryName("getByWord"));
        return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{word}, new CardMapper()));
    }

    @Transactional(readOnly = true)
    public Card getByOrder(int order) {
        final String sql = sqlResolver.getSql(queryName("getByOrder"));
        return jdbcTemplate.queryForObject(sql, new Object[]{order}, new CardMapper());
    }

    @Transactional(readOnly = true)
    public Optional<Card> getNotAttemptedByUser(int userId) {
        final String sql = sqlResolver.getSql(queryName("getNotAttemptedByUser"));

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{userId}, new CardMapper()));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public int getCardCount() {
        final String sql = sqlResolver.getSql(queryName("getCardCount"));
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Card save(final Card card) {
        if (StringUtils.isEmpty(card.getId())) {
            card.setId(UUID.randomUUID().toString());
            final String sql = sqlResolver.getSql(queryName("insert"));

            try {
                jdbcTemplate.update(sql, new Object[]{
                        card.getId(),
                        card.getWord(),
                        card.getTranslation(),
                        card.getCategory(),
                        card.getSubject(),
                        card.getDescription(),
                        card.getTranscription()
                });
            } catch (DataAccessException ex) {
                log.error(ex.getMessage());
            }
        } else {
            final String sql = "update card set word=?, word_translation=?, category=?, subject=?, description=?, wordOrder=? where id=?";
            throw new UnsupportedOperationException("update not implemented");
        }

        return getById(card.getId());
    }

    public List<Card> getAllCards() {
        final String sql = sqlResolver.getSql(queryName("getAllCards"));
        return jdbcTemplate.query(sql, new CardMapper());
    }

    public void backup(String filename) {
        final String sql = String.format("SCRIPT TO \'%s\'", filename);
        log.info(sql);

        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    protected String getQueryPrefix() {
        return "card";
    }
}

   /* public void fetchUsers(final UserSearchCriteria searchCriteria,
                           final BatchProcessor<User> batchProcessor,
                           final int batchSize) {
        String sql = sqlResolver.getSql(getQueryPrefix() + "fetch.users");
        if (searchCriteria != null) {
            sql += "where 1=1";
            if (searchCriteria.getChunkStart() > 0) {
                sql += " and id >= ?";
            }
            if (searchCriteria.getChunkEnd() > 0) {
                sql += " and id <= ?";
            }
        }
        realJdbcTemplate.query(new StreamingStatementCreator(sql, batchSize),
                ps -> {
                    if (searchCriteria != null) {
                        int num = 1;
                        if (searchCriteria.getChunkStart() > 0) {
                            ps.setLong(num++, searchCriteria.getChunkStart());
                        }
                        if (searchCriteria.getChunkEnd() > 0) {
                            ps.setLong(num++, searchCriteria.getChunkEnd());
                        }
                    }
                }, rs -> {
                    List<User> users = new ArrayList<>(batchSize);
                    int rowNum = 0;
                    while (rs.next()) {
                        users.add(getMapper().mapRow(rs, rowNum++));

                        if (rowNum > 0 && rowNum % batchSize == 0) {
                            batchProcessor.process(users);
                            users.clear();
                        }
                    }
                    if (!users.isEmpty()) {
                        batchProcessor.process(users);
                    }

                    return null;
                });
    }*/