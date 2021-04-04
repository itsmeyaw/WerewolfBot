package com.itsmeyaw.werewolfbot.data.server;

import javax.persistence.*;

@Entity
@Table(name="discordserver")
public class DiscordServerEntity {
    @Id
    private Long snowflake; // Snowflake == long!!

    @Column(nullable = false)
    private String prefix;

    public DiscordServerEntity() {}

    public DiscordServerEntity(long snowflake, String prefix) {
        this.snowflake = snowflake;
        this.prefix = prefix;
    }

    public Long getSnowflake() {
        return snowflake;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "DiscordServer[" +
                "snowflake=" + snowflake +
                ", prefix='" + prefix + '\'' +
                ']';
    }
}
