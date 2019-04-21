package ru.gecec.learnphrasebot.model.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.gecec.learnphrasebot.model.entity.UserAttempt;
import ru.gecec.learnphrasebot.model.mapper.UserAttemptMapper;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserAttemptsRepository extends BaseRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserAttemptsRepository.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserAttempt save(final UserAttempt attempt) {
        if (StringUtils.isEmpty(attempt.getId())) {
            attempt.setId(UUID.randomUUID().toString());

            final String sql = sqlResolver.getSql(queryName("insert"));

            try {
                jdbcTemplate.update(sql, new Object[]{
                        attempt.getId(),
                        attempt.getUserId(),
                        attempt.getCardId(),
                        attempt.getWordSuccess(),
                        attempt.getWordFailure(),
                        attempt.getTranslationSuccess(),
                        attempt.getTranslationFailure(),
                        new Date()
                });
            } catch (DataAccessException ex) {
                LOGGER.error("Can't create attempt", ex);
            }
        } else {
            final String sql = sqlResolver.getSql(queryName("update"));

            try {
                jdbcTemplate.update(sql, new Object[]{
                        attempt.getId(),
                        attempt.getUserId(),
                        attempt.getCardId(),
                        attempt.getWordSuccess(),
                        attempt.getWordFailure(),
                        attempt.getTranslationSuccess(),
                        attempt.getTranslationFailure(),
                        new Date()
                });
            } catch (DataAccessException ex) {
                LOGGER.error("Can't update attempt", ex);
            }

        }

        return getById(attempt.getId());
    }

    public UserAttempt getById(String id){
        final String sql = sqlResolver.getSql(queryName("getById"));
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserAttemptMapper());
    }

    public Optional<UserAttempt> getByUserIdAndCardId(int userId, String cardId){
        try {
            final String sql = sqlResolver.getSql(queryName("getByUserIdAndCardId"));
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{userId, cardId}, new UserAttemptMapper()));
        } catch (EmptyResultDataAccessException ex){
            return Optional.empty();
        }
    }

    public Optional<UserAttempt> getWorstWord(int userId){
        try {
            final String sql = sqlResolver.getSql(queryName("getWorstWord"));
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{userId}, new UserAttemptMapper()));
        } catch (EmptyResultDataAccessException ex){
            return Optional.empty();
        }
    }

    public Optional<UserAttempt> getWorstTranslation(int userId){
        try {
            final String sql = sqlResolver.getSql(queryName("getWorstTranslation"));
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{userId}, new UserAttemptMapper()));
        } catch (EmptyResultDataAccessException ex){
            return Optional.empty();
        }
    }

    @Override
    protected String getQueryPrefix() {
        return "user.attempt";
    }
}
