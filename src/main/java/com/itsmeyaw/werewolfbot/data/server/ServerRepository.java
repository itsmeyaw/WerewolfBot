package com.itsmeyaw.werewolfbot.data.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ServerRepository extends JpaRepository<DiscordServerEntity, Long> {
    DiscordServerEntity findBySnowflake(long snowflake);
}
