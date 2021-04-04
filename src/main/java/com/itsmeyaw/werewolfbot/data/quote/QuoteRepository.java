package com.itsmeyaw.werewolfbot.data.quote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {
    List<QuoteEntity> findAllBySnowflake(long snowflake);
    QuoteEntity findBySnowflakeAndQuoteContaining(long snowflake, String quote);
    List<QuoteEntity> findAllBySnowflakeAndQuoteMaker(long snowflake, String quoteMaker);
}
